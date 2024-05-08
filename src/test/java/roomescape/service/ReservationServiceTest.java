package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.reservation.ReservationTime;
import roomescape.exception.AlreadyExistsException;
import roomescape.exception.NotExistException;
import roomescape.exception.PastTimeReservationException;
import roomescape.fixture.ThemeFixture;
import roomescape.service.dto.input.ReservationInput;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    ReservationTimeDao reservationTimeDao;

    @Autowired
    ReservationService reservationService;

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

    @Test
    @DisplayName("유효한 값을 입력하면 예외를 발생하지 않는다")
    void create_reservation() {
        long timeId = reservationTimeDao.create(ReservationTime.from(null, "10:00"))
                .getId();
        Long themeId = themeDao.create(ThemeFixture.getDomain())
                .getId();
        ReservationInput input = new ReservationInput("jerry", "2023-03-13", timeId, themeId);

        assertThatCode(() -> reservationService.createReservation(input))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하지 않는 예약 ID 를 삭제하려 하면 에외를 발생한다.")
    void throw_exception_when_not_exist_id() {
        assertThatThrownBy(() -> reservationService.deleteReservation(-1))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    @DisplayName("중복 예약 이면 예외를 발생한다.")
    void throw_exception_when_duplicate_reservationTime() {
        long timeId = reservationTimeDao.create(ReservationTime.from(null, "10:00"))
                .getId();
        Long themeId = themeDao.create(ThemeFixture.getDomain())
                .getId();
        reservationService.createReservation(new ReservationInput("제리", "2011-11-24", timeId, themeId));
        final var input = new ReservationInput("제리", "2011-11-24", timeId, themeId);

        assertThatThrownBy(
                () -> reservationService.createReservation(input))
                .isInstanceOf(AlreadyExistsException.class);
    }

    @Test
    @DisplayName("지나간 날짜와 시간으로 예약 생성 시 예외가 발생한다.")
    void throw_exception_when_create_past_time_reservation() {
        Long timeId = reservationTimeDao.create(ReservationTime.from(null, "10:00"))
                .getId();
        Long themeId = themeDao.create(ThemeFixture.getDomain())
                .getId();
        final var input = new ReservationInput("제리", "1300-03-10", timeId, themeId);

        assertThatThrownBy(
                () -> reservationService.createReservation(input))
                .isInstanceOf(PastTimeReservationException.class);
    }
}
