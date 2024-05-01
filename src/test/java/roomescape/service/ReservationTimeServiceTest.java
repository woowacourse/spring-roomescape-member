package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.dao.WebReservationDao;
import roomescape.dao.WebReservationTimeDao;
import roomescape.dao.WebThemeDao;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservation.ReservationName;
import roomescape.domain.reservationtime.ReservationStartAt;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.dto.reservationtime.ReservationTimeCreateRequest;
import roomescape.dto.reservationtime.ReservationTimeResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ReservationTimeServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private WebReservationDao reservationDao;
    @Autowired
    private WebReservationTimeDao reservationTimeDao;
    @Autowired
    private WebThemeDao themeDao;
    @Autowired
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        reservationTimeDao.create(new ReservationTime(null, ReservationStartAt.from("12:02")));
        reservationTimeDao.create(new ReservationTime(null, ReservationStartAt.from("12:42")));
        themeDao.create(new Theme(null, "방탈출1", "방탈출 1번", "섬네일1"));
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    @DisplayName("모든 예약 시간 정보를 조회한다.")
    void findAll() {
        //when
        List<ReservationTimeResponse> results = reservationTimeService.findAll();
        ReservationTimeResponse firstResponse = results.get(0);

        //then
        assertAll(
                () -> assertThat(results).hasSize(2),
                () -> assertThat(firstResponse.getId()).isEqualTo(1),
                () -> assertThat(firstResponse.getStartAt()).isEqualTo("12:02")
        );
    }

    @Nested
    @DisplayName("예약 시간 추가")
    class create {
        @Test
        @DisplayName("예약 시간을 추가한다.")
        void add() {
            //given
            String givenStartAt = "10:52";
            ReservationTimeCreateRequest request = ReservationTimeCreateRequest.from(givenStartAt);

            //when
            ReservationTimeResponse result = reservationTimeService.add(request);

            //then
            assertAll(
                    () -> assertThat(result.getId()).isEqualTo(3),
                    () -> assertThat(result.getStartAt()).isEqualTo(givenStartAt),
                    () -> assertThat(reservationTimeService.findAll()).hasSize(3)
            );
        }


        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("예약 시간에 null이나 공백 문자열이 입력되면 예외가 발생한다.")
        void createReservationTimeByNullOrEmptyStartAt(String given) {
            //given
            ReservationTimeCreateRequest request = ReservationTimeCreateRequest.from(given);

            //when //then
            assertThatThrownBy(() -> reservationTimeService.add(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예약 시간이 중복되면 예외가 발생한다.")
        void createReservationTimeWhenDuplicatedStartAt() {
            //given
            ReservationTimeCreateRequest request = ReservationTimeCreateRequest.from("12:02");

            //when //then
            assertThatThrownBy(() -> reservationTimeService.add(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("예약 시간 삭제")
    class delete {
        @Test
        @DisplayName("예약 시간을 삭제한다.")
        void delete() {
            //given
            long givenId = 1L;

            //when
            reservationTimeService.delete(givenId);
            List<ReservationTimeResponse> results = reservationTimeService.findAll();
            ReservationTimeResponse secondResponse = results.get(0);

            //then
            assertAll(
                    () -> assertThat(results).hasSize(1),
                    () -> assertThat(secondResponse.getId()).isEqualTo(2)
            );
        }

        @Test
        @DisplayName("예약 시간 삭제시 아이디가 비어있으면 예외가 발생한다.")
        void deleteNullId() {
            //given
            Long givenId = null;

            //when //then
            assertThatThrownBy(() -> reservationTimeService.delete(givenId))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예약 시간 삭제시 아이디가 존재하지 않는다면 예외가 발생한다.")
        void deleteNotExistId() {
            //given
            long givenId = 3L;

            //when //then
            assertThatThrownBy(() -> reservationTimeService.delete(givenId))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때 예외가 발생한다.")
        void deleteReservationTimeWhenReservationExist() {
            //given
            reservationDao.create(new Reservation(
                    null,
                    new ReservationName("다온"),
                    ReservationDate.from(LocalDate.now().plusDays(1).toString()),
                    new ReservationTime(1L, ReservationStartAt.from("12:02")),
                    new Theme(1L, "방탈출1", "방탈출 1번", "섬네일1")));

            //when //then
            assertThatThrownBy(() -> reservationTimeService.delete(1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
