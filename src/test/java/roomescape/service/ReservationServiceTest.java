package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
import roomescape.dao.JdbcReservationDao;
import roomescape.dao.JdbcReservationTimeDao;
import roomescape.dao.JdbcThemeDao;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.dto.reservation.AvailableReservationResponse;
import roomescape.dto.reservation.ReservationCreateRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.domain.exception.InvalidValueException;
import roomescape.fixture.ReservationFixtures;
import roomescape.fixture.ReservationTimeFixtures;
import roomescape.fixture.ThemeFixtures;
import roomescape.service.exception.InvalidRequestException;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ReservationServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcReservationDao reservationDao;
    @Autowired
    private JdbcReservationTimeDao reservationTimeDao;
    @Autowired
    private JdbcThemeDao themeDao;
    @Autowired
    private ReservationService reservationService;
    private final String tomorrow = LocalDate.now().plusDays(1).toString();

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    @DisplayName("모든 예약 정보를 조회한다.")
    void findAll() {
        //given
        Theme theme = themeDao.create(ThemeFixtures.createDefaultTheme());
        ReservationTime time1 = reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:02"));
        ReservationTime time2 = reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:20"));
        Reservation reservation1 = ReservationFixtures.createReservation(time1, theme);
        Reservation reservation2 = ReservationFixtures.createReservation(time2, theme);
        reservationDao.create(reservation1);
        reservationDao.create(reservation2);

        //when
        List<ReservationResponse> results = reservationService.findAll();

        //then
        assertThat(results).hasSize(2);

    }

    @Test
    @DisplayName("이용 가능한 예약 시간을 조회한다.")
    void findTimeByDateAndThemeId() {
        //given
        Theme theme = themeDao.create(ThemeFixtures.createDefaultTheme());
        ReservationTime time1 = reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:02"));
        ReservationTime time2 = reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:20"));
        Reservation reservation = ReservationFixtures.createReservation(tomorrow, time1, theme);
        reservationDao.create(reservation);

        //when
        List<AvailableReservationResponse> responses
                = reservationService.findTimeByDateAndThemeID(tomorrow, 1L);
        AvailableReservationResponse alreadyBooked = responses.get(0);
        AvailableReservationResponse notBooked = responses.get(1);

        //then
        assertAll(
                () -> assertThat(alreadyBooked.alreadyBooked()).isTrue(),
                () -> assertThat(notBooked.alreadyBooked()).isFalse()
        );
    }

    @Nested
    @DisplayName("예약 추가")
    class create {
        @Test
        @DisplayName("예약을 추가한다.")
        void add() {
            //given
            String givenName = "wooteco" ;
            String givenDate = tomorrow;
            ReservationTime reservationTime =
                    reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            Theme theme = themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest givenRequest =
                    ReservationFixtures.createReservationCreateRequest(
                            givenName, givenDate, reservationTime.getId(), theme.getId()
                    );

            //when
            ReservationResponse result = reservationService.add(givenRequest);

            //then
            assertAll(
                    () -> assertThat(result.name()).isEqualTo(givenName),
                    () -> assertThat(result.date()).isEqualTo(givenDate),
                    () -> assertThat(reservationService.findAll()).hasSize(1)
            );
        }

        @Test
        @DisplayName("존재하지 않는 시간 아이디로 예약 추가시 에외가 발생한다.")
        void addNotExistTimeId() {
            //given
            themeDao.create(ThemeFixtures.createDefaultTheme());
            Long given = -1L;
            ReservationCreateRequest givenRequest = ReservationFixtures.createReservationCreateRequest(given, 1L);

            //when //then
            assertThatThrownBy(() -> reservationService.add(givenRequest))
                    .isInstanceOf(InvalidRequestException.class);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("예약자명에 null이나 공백 문자열이 입력되면 예외가 발생한다.")
        void createReservationByNullOrEmptyName(String given) {
            //given
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request =
                    ReservationFixtures.createReservationCreateRequest(given, tomorrow, 1L, 1L);

            //when //then
            assertThatThrownBy(() -> reservationService.add(request))
                    .isInstanceOf(InvalidValueException.class);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("예약 날짜에 null이나 공백 문자열이 입력되면 예외가 발생한다.")
        void createReservationByNullOrEmptyDate(String given) {
            //given
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request =
                    ReservationFixtures.createReservationCreateRequest("다온", given, 1L, 1L);

            //when //then
            assertThatThrownBy(() -> reservationService.add(request))
                    .isInstanceOf(InvalidValueException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {"24-02-04", "2024;04;24"})
        @DisplayName("예약 날짜가 yyyy-MM-dd 형식이 아닌 경우 예외가 발생한다.")
        void createReservationByInvalidDate(String given) {
            //given
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request =
                    ReservationFixtures.createReservationCreateRequest("다온", given, 1L, 1L);

            //when //then
            assertThatThrownBy(() -> reservationService.add(request))
                    .isInstanceOf(InvalidValueException.class);
        }

        @Test
        @DisplayName("지나간 날짜에 대한 예약을 추가하면 예외가 발생한다.")
        void createReservationByPastDate() {
            //given
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request =
                    ReservationFixtures.createReservationCreateRequest("다온", "2024-04-29", 1L, 1L);

            //when //then
            assertThatThrownBy(() -> reservationService.add(request))
                    .isInstanceOf(InvalidRequestException.class);
        }

        @Test
        @DisplayName("지나간 시간에 대한 예약을 추가하면 예외가 발생한다.")
        void createReservationByPastTime() {
            //given
            String pastTime = LocalTime.now().minusHours(1).toString();
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime(pastTime));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request =
                    ReservationFixtures.createReservationCreateRequest("다온", LocalDate.now().toString(), 1L, 1L);

            //when //then
            assertThatThrownBy(() -> reservationService.add(request))
                    .isInstanceOf(InvalidRequestException.class);
        }

        @Test
        @DisplayName("동일한 테마, 날짜, 시간에 대한 예약을 추가하면 예외가 발생한다.")
        void createReservationByDuplicated() {
            //given
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            ReservationCreateRequest request = ReservationFixtures.createReservationCreateRequest("다온", tomorrow, 1L,
                    1L);
            reservationService.add(request);

            //when //then
            assertThatThrownBy(() -> reservationService.add(request))
                    .isInstanceOf(InvalidRequestException.class);
        }
    }

    @Nested
    @DisplayName("예약 삭제")
    class delete {
        @Test
        @DisplayName("예약을 삭제한다.")
        void deleteReservation() {
            //given
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            reservationService.add(ReservationFixtures.createReservationCreateRequest("다온", tomorrow, 1L, 1L));
            long givenId = 1L;

            //when
            reservationService.delete(givenId);
            List<ReservationResponse> results = reservationService.findAll();

            //then
            assertThat(results).isEmpty();
        }

        @Test
        @DisplayName("예약 삭제시 아이디가 존재하지 않는다면 예외가 발생한다.")
        void deleteNotExistId() {
            //given
            reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:00"));
            themeDao.create(ThemeFixtures.createDefaultTheme());
            reservationService.add(ReservationFixtures.createReservationCreateRequest("다온", tomorrow, 1L, 1L));
            long givenId = -1L;

            //when //then
            assertThatThrownBy(() -> reservationService.delete(givenId))
                    .isInstanceOf(InvalidRequestException.class);
        }
    }
}
