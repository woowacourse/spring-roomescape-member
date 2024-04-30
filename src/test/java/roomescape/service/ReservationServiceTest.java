package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.dao.WebReservationDao;
import roomescape.dao.WebReservationTimeDao;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservation.ReservationName;
import roomescape.domain.reservationtime.ReservationStartAt;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.dto.reservation.ReservationCreateRequest;
import roomescape.dto.reservation.ReservationResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ReservationServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private WebReservationDao reservationDao;
    @Autowired
    private WebReservationTimeDao reservationTimeDao;
    @Autowired
    private ReservationService reservationService;
    private final String tomorrow = LocalDate.now().plusDays(1).toString();

    @BeforeEach
    void setUp() {
        Reservation daon = new Reservation(
                null,
                new ReservationName("daon"),
                ReservationDate.from(tomorrow),
                reservationTimeDao.create(new ReservationTime(null, ReservationStartAt.from("23:00")))
        );
        Reservation ikjo = new Reservation(
                null,
                new ReservationName("ikjo"),
                ReservationDate.from(tomorrow),
                reservationTimeDao.create(new ReservationTime(null, ReservationStartAt.from("22:00")))
        );
        reservationDao.create(daon);
        reservationDao.create(ikjo);
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    @DisplayName("모든 예약 정보를 조회한다.")
    void findAll() {
        //when
        List<ReservationResponse> results = reservationService.findAll();
        ReservationResponse firstResponse = results.get(0);

        //then
        assertAll(
                () -> assertThat(results).hasSize(2),
                () -> assertThat(firstResponse.getId()).isEqualTo(1),
                () -> assertThat(firstResponse.getName()).isEqualTo("daon")
        );
    }

    @Nested
    @DisplayName("예약 추가")
    class create {
        @Test
        @DisplayName("예약을 추가한다.")
        void add() {
            //given
            String givenName = "wooteco";
            String givenDate = tomorrow;
            ReservationTime reservationTime = reservationTimeDao.create(
                    new ReservationTime(null, ReservationStartAt.from("12:00")));
            ReservationCreateRequest givenRequest =
                    ReservationCreateRequest.of(givenName, givenDate, reservationTime.getId());

            //when
            ReservationResponse result = reservationService.add(givenRequest);

            //then
            assertAll(
                    () -> assertThat(result.getId()).isEqualTo(3),
                    () -> assertThat(result.getName()).isEqualTo(givenName),
                    () -> assertThat(result.getDate()).isEqualTo(givenDate),
                    () -> assertThat(reservationService.findAll()).hasSize(3)
            );
        }

        @Test
        @DisplayName("존재하지 않는 시간 아이디로 예약 추가시 에외가 발생한다.")
        void addNotExistTimeId() {
            //given
            Long given = -1L;
            String givenName = "wooteco";
            String givenDate = tomorrow;
            ReservationCreateRequest givenRequest = ReservationCreateRequest.of(givenName, givenDate, given);

            //when //then
            assertThatThrownBy(() -> reservationService.add(givenRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("예약자명에 null이나 공백 문자열이 입력되면 예외가 발생한다.")
        void createReservationByNullOrEmptyName(String given) {
            //given
            ReservationCreateRequest request = ReservationCreateRequest.of(given, tomorrow, 1L);

            //when //then
            assertThatThrownBy(() -> reservationService.add(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("예약 날짜에 null이나 공백 문자열이 입력되면 예외가 발생한다.")
        void createReservationByNullOrEmptyDate(String given) {
            //given
            ReservationCreateRequest request = ReservationCreateRequest.of("다온", given, 1L);

            //when //then
            assertThatThrownBy(() -> reservationService.add(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {"24-02-04", "2024;04;24"})
        @DisplayName("예약 날짜가 yyyy-MM-dd 형식이 아닌 경우 예외가 발생한다.")
        void createReservationByInvalidDate(String given) {
            //given
            ReservationCreateRequest request = ReservationCreateRequest.of("다온", given, 1L);

            //when //then
            assertThatThrownBy(() -> reservationService.add(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("지나간 날짜에 대한 예약을 추가하면 예외가 발생한다.")
        void createReservationByPastDate() {
            //given
            ReservationCreateRequest request = ReservationCreateRequest.of("다온", "2024-04-29", 1L);

            //when //then
            assertThatThrownBy(() -> reservationService.add(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("지나간 시간에 대한 예약을 추가하면 예외가 발생한다.")
        void createReservationByPastTime() {
            //given
            String pastTime = LocalTime.now().minusHours(1).toString();
            ReservationTime reservationTime = reservationTimeDao.create(
                    new ReservationTime(null, ReservationStartAt.from(pastTime)));
            ReservationCreateRequest request =
                    ReservationCreateRequest.of("다온", LocalDate.now().toString(), reservationTime.getId());

            //when //then
            assertThatThrownBy(() -> reservationService.add(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("동일한 날짜와 시간에 대한 예약을 추가하면 예외가 발생한다.")
        void createReservationByDuplicated() {
            //given
            ReservationCreateRequest request = ReservationCreateRequest.of("다온", tomorrow, 1L);

            //when //then
            assertThatThrownBy(() -> reservationService.add(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("예약 삭제")
    class delete {
        @Test
        @DisplayName("예약을 삭제한다.")
        void deleteReservation() {
            //given
            long givenId = 1L;

            //when
            reservationService.delete(givenId);
            List<ReservationResponse> results = reservationService.findAll();
            ReservationResponse secondResponse = results.get(0);

            //then
            assertAll(
                    () -> assertThat(results).hasSize(1),
                    () -> assertThat(secondResponse.getId()).isEqualTo(2)
            );
        }

        @Test
        @DisplayName("예약 삭제시 아이디가 비어있으면 예외가 발생한다.")
        void deleteNullId() {
            //given
            Long givenId = null;

            //when //then
            assertThatThrownBy(() -> reservationService.delete(givenId))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예약 삭제시 아이디가 존재하지 않는다면 예외가 발생한다.")
        void deleteNotExistId() {
            //given
            long givenId = 3L;

            //when //then
            assertThatThrownBy(() -> reservationService.delete(givenId))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
