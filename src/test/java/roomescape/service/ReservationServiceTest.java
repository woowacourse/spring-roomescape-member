package roomescape.service;

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
import roomescape.exception.CustomException2;
import roomescape.exception.NotExistsException;
import roomescape.fixture.MemberFixture;
import roomescape.fixture.ReservationFixture;
import roomescape.fixture.ReservationTimeFixture;
import roomescape.fixture.ThemeFixture;
import roomescape.service.dto.input.ReservationInput;

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
        final var memberId = memberDao.create(MemberFixture.getDomain("제리")).id();
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
                .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("중복 예약이면 예외를 발생한다.")
    @Test
    void throw_exception_when_duplicate_reservationTime() {
        final var member = memberDao.create(MemberFixture.getDomain("제리"));
        final var time = reservationTimeDao.create(ReservationTimeFixture.getDomain());
        final var theme = themeDao.create(ThemeFixture.getDomain("테마 1"));
        reservationDao.create(ReservationFixture.getDomain(member, time, theme));

        final ReservationInput input = ReservationFixture.getInput(time.id(), theme.id(), member.id());
        assertThatThrownBy(() -> reservationService.createReservation(input))
                .isInstanceOf(CustomException2.class);
    }

    @DisplayName("지나간 날짜와 시간으로 예약 생성 시 예외가 발생한다.")
    @Test
    void throw_exception_when_create_past_time_reservation() {
        final var timeId = reservationTimeDao.create(ReservationTimeFixture.getDomain()).id();
        final var themeId = themeDao.create(ThemeFixture.getDomain("테마 1")).id();
        final var memberId = memberDao.create(MemberFixture.getDomain("제리")).id();

        final ReservationInput input = ReservationInput.of("2024-01-01", timeId, themeId, memberId);
        assertThatThrownBy(() -> reservationService.createReservation(input))
                .isInstanceOf(CustomException2.class);
    }
}
