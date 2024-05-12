package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.exception.CustomBadRequest;
import roomescape.exception.CustomException;
import roomescape.fixture.MemberFixture;
import roomescape.fixture.ReservationFixture;
import roomescape.fixture.ReservationTimeFixture;
import roomescape.fixture.ThemeFixture;
import roomescape.service.dto.input.ReservationInput;
import roomescape.service.dto.output.ReservationOutput;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationDao reservationDao;
    @Autowired
    ReservationTimeDao reservationTimeDao;
    @Autowired
    ThemeDao themeDao;
    @Autowired
    MemberDao memberDao;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("TRUNCATE TABLE reservation");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.update("TRUNCATE TABLE reservation_time");
        jdbcTemplate.update("TRUNCATE TABLE theme");
        jdbcTemplate.update("TRUNCATE TABLE member");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @DisplayName("예약을 생성한다.")
    @Test
    void create_reservation() {
        final var memberId = memberDao.create(MemberFixture.getDomain()).id();
        final var timeId = reservationTimeDao.create(ReservationTimeFixture.getDomain()).id();
        final var themeId = themeDao.create(ThemeFixture.getDomain("테마 1")).id();

        final ReservationInput input = ReservationFixture.getInput(timeId, themeId, memberId);
        assertThatCode(() -> reservationService.createReservation(input))
                .doesNotThrowAnyException();
    }

    @DisplayName("존재하지 않는 예약 ID 를 삭제하려 하면 에외를 발생한다.")
    @Test
    void throw_exception_when_not_exist_id() {
        assertThatThrownBy(() -> reservationService.deleteReservation(-1))
                .isInstanceOf(CustomBadRequest.class);
    }

    @DisplayName("중복 예약이면 예외를 발생한다.")
    @Test
    void throw_exception_when_duplicate_reservationTime() {
        final var member = memberDao.create(MemberFixture.getDomain());
        final var time = reservationTimeDao.create(ReservationTimeFixture.getDomain());
        final var theme = themeDao.create(ThemeFixture.getDomain("테마 1"));
        reservationDao.create(ReservationFixture.getDomain(member, time, theme));

        final ReservationInput input = ReservationFixture.getInput(time.id(), theme.id(), member.id());
        assertThatThrownBy(() -> reservationService.createReservation(input))
                .isInstanceOf(CustomException.class);
    }

    @DisplayName("지나간 날짜와 시간으로 예약 생성 시 예외가 발생한다.")
    @Test
    void throw_exception_when_create_past_time_reservation() {
        final var timeId = reservationTimeDao.create(ReservationTimeFixture.getDomain()).id();
        final var themeId = themeDao.create(ThemeFixture.getDomain("테마 1")).id();
        final var memberId = memberDao.create(MemberFixture.getDomain()).id();

        final ReservationInput input = ReservationInput.of("2024-01-01", timeId, themeId, memberId);
        assertThatThrownBy(() -> reservationService.createReservation(input))
                .isInstanceOf(CustomException.class);
    }

    @DisplayName("dateFrom이 없고 dateTo가 없는 경우 전체 날짜를 조회한다.")
    @Test
    void get_all_date_reservations() {
        final var time = reservationTimeDao.create(ReservationTimeFixture.getDomain());
        final var member = memberDao.create(MemberFixture.getDomain());
        final var theme = themeDao.create(ThemeFixture.getDomain("테마 1"));

        final var reservation1 = reservationDao.create(Reservation.of(null, member, "2024-06-02", time, theme));
        final var reservation2 = reservationDao.create(Reservation.of(null, member, "2024-06-03", time, theme));

        final var outputs = reservationService.filterReservations(null, null, null, null);

        assertThat(outputs).containsExactly(ReservationOutput.from(reservation1), ReservationOutput.from(reservation2));
    }

    @DisplayName("dateFrom이 없고 dateTo가 있는 경우 dateTo까지의 날짜를 조회한다.")
    @Test
    void get_until_dateTo_reservations() {
        final var time = reservationTimeDao.create(ReservationTimeFixture.getDomain());
        final var member = memberDao.create(MemberFixture.getDomain());
        final var theme = themeDao.create(ThemeFixture.getDomain("테마 1"));

        final var reservation1 = reservationDao.create(Reservation.of(null, member, "2024-06-02", time, theme));
        final var reservation2 = reservationDao.create(Reservation.of(null, member, "2024-06-03", time, theme));

        final var outputs = reservationService.filterReservations(null, null, null, "2024-06-02");

        assertThat(outputs).containsExactly(ReservationOutput.from(reservation1));
    }

    @DisplayName("dateFrom이 있고 dateTo가 없는 경우 dateFrom부터의 날짜를 조회한다.")
    @Test
    void get_after_dateFrom_reservations() {
        final var time = reservationTimeDao.create(ReservationTimeFixture.getDomain());
        final var member = memberDao.create(MemberFixture.getDomain());
        final var theme = themeDao.create(ThemeFixture.getDomain("테마 1"));

        final var reservation1 = reservationDao.create(Reservation.of(null, member, "2024-06-02", time, theme));
        final var reservation2 = reservationDao.create(Reservation.of(null, member, "2024-06-03", time, theme));

        final var outputs = reservationService.filterReservations(null, null, "2024-06-03", null);

        assertThat(outputs).containsExactly(ReservationOutput.from(reservation2));
    }

    @DisplayName("dateFrom이 있고 dateTo가 있는 경우 dateFrom부터 dateTo까지의 날짜를 조회한다.")
    @Test
    void get_after_dateFrom_and_until_dateTo_reservations() {
        final var time = reservationTimeDao.create(ReservationTimeFixture.getDomain());
        final var member = memberDao.create(MemberFixture.getDomain());
        final var theme = themeDao.create(ThemeFixture.getDomain("테마 1"));

        final var reservation1 = reservationDao.create(Reservation.of(null, member, "2024-06-02", time, theme));
        final var reservation2 = reservationDao.create(Reservation.of(null, member, "2024-06-03", time, theme));
        final var reservation3 = reservationDao.create(Reservation.of(null, member, "2024-06-04", time, theme));

        final var outputs = reservationService.filterReservations(null, null, "2024-06-03", "2024-06-04");

        assertThat(outputs).containsExactly(ReservationOutput.from(reservation2), ReservationOutput.from(reservation3));
    }
}
