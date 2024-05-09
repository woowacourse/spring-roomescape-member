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
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.ExistsException;
import roomescape.exception.InvalidInputException;
import roomescape.exception.NotExistsException;
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
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("TRUNCATE TABLE reservation");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.update("TRUNCATE TABLE reservation_time");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @DisplayName("예약을 생성한다.")
    @Test
    void create_reservation() {
        final long timeId = reservationTimeDao.create(ReservationTimeFixture.getDomain()).getId();
        final long themeId = themeDao.create(ThemeFixture.getDomain("테마 1")).getId();

        final ReservationInput input = ReservationFixture.getInput(timeId, themeId);
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
        final ReservationTime time = reservationTimeDao.create(ReservationTimeFixture.getDomain());
        final Theme theme = themeDao.create(ThemeFixture.getDomain("테마 1"));
        reservationDao.create(ReservationFixture.getDomain(time, theme));

        final ReservationInput input = ReservationFixture.getInput(time.getId(), theme.getId());
        assertThatThrownBy(() -> reservationService.createReservation(input))
                .isInstanceOf(ExistsException.class);
    }

    @DisplayName("지나간 날짜와 시간으로 예약 생성 시 예외가 발생한다.")
    @Test
    void throw_exception_when_create_past_time_reservation() {
        final Long timeId = reservationTimeDao.create(ReservationTimeFixture.getDomain()).getId();
        final Long themeId = themeDao.create(ThemeFixture.getDomain("테마 1")).getId();

        final ReservationInput input = new ReservationInput("제리", "2024-01-01", timeId, themeId);
        assertThatThrownBy(() -> reservationService.createReservation(input))
                .isInstanceOf(InvalidInputException.class);
    }
}
