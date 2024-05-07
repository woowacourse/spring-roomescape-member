package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.TimeRepository;

@JdbcTest
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private ReservationService reservationService;
    private TimeRepository timeRepository;
    private ThemeRepository themeRepository;
    private ReservationRepository reservationRepository;

    @BeforeEach
    void init() {
        reservationRepository = new ReservationRepository(jdbcTemplate, dataSource);
        timeRepository = new TimeRepository(jdbcTemplate, dataSource);
        themeRepository = new ThemeRepository(jdbcTemplate, dataSource);
        reservationService = new ReservationService(reservationRepository, timeRepository, themeRepository);
    }

    @Test
    @DisplayName("동일한 날짜와 시간과 테마에 예약을 생성하면 예외가 발생한다")
    void duplicateTimeReservationAddFail() {
        // given
        Time time = timeRepository.save(new Time(LocalTime.of(12, 30)));
        Theme theme = themeRepository.save(new Theme("테마명", "설명", "썸네일URL"));

        // when & then
        reservationService.createReservation(
                new ReservationRequest("예약", LocalDate.now().plusDays(1L), time.getId(), theme.getId()));

        assertThatThrownBy(() -> reservationService.createReservation(
                new ReservationRequest("예약", LocalDate.now().plusDays(1L), time.getId(), theme.getId())))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("이미 지난 날짜로 예약을 생성하면 예외가 발생한다")
    void beforeDateReservationFail() {
        // given
        Time time = timeRepository.save(new Time(LocalTime.of(12, 30)));
        Theme theme = themeRepository.save(new Theme("테마명", "설명", "썸네일URL"));
        LocalDate beforeDate = LocalDate.now().minusDays(1L);

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(
                new ReservationRequest("예약", beforeDate, time.getId(), theme.getId())))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("현재 날짜가 예약 당일이지만, 이미 지난 시간으로 예약을 생성하면 예외가 발생한다")
    void beforeTimeReservationFail() {
        // given
        LocalTime requestTime = LocalTime.now();
        LocalTime beforeTime = requestTime.minusMinutes(1L);
        Time time = timeRepository.save(new Time(beforeTime));
        Theme theme = themeRepository.save(new Theme("테마명", "설명", "썸네일URL"));

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(
                new ReservationRequest("예약", LocalDate.now(), time.getId(), theme.getId()))
        ).isInstanceOf(IllegalStateException.class);
    }
}
