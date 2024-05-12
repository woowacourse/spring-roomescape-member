package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
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
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.CustomBadRequest;
import roomescape.fixture.MemberFixture;
import roomescape.fixture.ReservationFixture;
import roomescape.fixture.ReservationTimeFixture;
import roomescape.fixture.ThemeFixture;
import roomescape.service.dto.input.ReservationTimeInput;
import roomescape.service.dto.output.AvailableReservationTimeOutput;

@SpringBootTest
@ActiveProfiles("test")
class ReservationTimeServiceTest {

    @Autowired
    ReservationTimeService reservationTimeService;
    @Autowired
    ReservationTimeDao reservationTimeDao;
    @Autowired
    ReservationDao reservationDao;
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

    @DisplayName("예약 시간을 생성한다.")
    @Test
    void create_reservationTime() {
        final ReservationTimeInput input = new ReservationTimeInput("10:00");

        assertThatCode(() -> reservationTimeService.createReservationTime(input))
                .doesNotThrowAnyException();
    }

    @DisplayName("유효하지 않은 값을 입력하면 예외가 발생한다.")
    @Test
    void throw_exception_when_input_is_invalid() {
        final ReservationTimeInput input = new ReservationTimeInput("");

        assertThatThrownBy(() -> reservationTimeService.createReservationTime(input))
                .isInstanceOf(CustomBadRequest.class);
    }

    @DisplayName("존재하지 않는 시간 ID 를 삭제하려 하면 에외가 발생한다.")
    @Test
    void throw_exception_when_not_exist_id() {
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(-1))
                .isInstanceOf(CustomBadRequest.class);
    }

    @DisplayName("예약이 존재하는 시간을 삭제하려 하면 예외가 발생한다.")
    @Test
    void throw_exception_when_delete_id_that_exist_reservation() {
        final var member = memberDao.create(MemberFixture.getDomain());
        final ReservationTime time = reservationTimeDao.create(ReservationTimeFixture.getDomain());
        final Theme theme = themeDao.create(ThemeFixture.getDomain("테마 1"));
        reservationDao.create(ReservationFixture.getDomain(member, time, theme));

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(time.id()))
                .isInstanceOf(CustomBadRequest.class);
    }

    @DisplayName("중복 예약 시간이면 예외를 발생한다.")
    @Test
    void throw_exception_when_duplicate_reservationTime() {
        reservationTimeDao.create(ReservationTimeFixture.getDomain());

        assertThatThrownBy(() -> reservationTimeService.createReservationTime(ReservationTimeFixture.getInput()))
                .isInstanceOf(CustomBadRequest.class);
    }

    @DisplayName("예약 가능한 시간을 조회한다.")
    @Test
    void get_available_reservationTime() {
        final var member = memberDao.create(MemberFixture.getDomain());
        final var time1 = reservationTimeDao.create(ReservationTimeFixture.getDomain());
        final var theme = themeDao.create(ThemeFixture.getDomain("테마 1"));
        reservationDao.create(ReservationFixture.getDomain(member, time1, theme));

        final var time2 = reservationTimeDao.create(ReservationTimeFixture.getDomain("11:00"));

        final List<AvailableReservationTimeOutput> actual =
                reservationTimeService.findAvailableReservationTimes("2024-06-01", theme.id());

        assertThat(actual).containsExactly(
                new AvailableReservationTimeOutput("10:00", time1.id(), true),
                new AvailableReservationTimeOutput("11:00", time2.id(), false)
        );
    }
}
