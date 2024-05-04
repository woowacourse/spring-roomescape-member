package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.global.exception.model.ConflictException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.TimeRepository;

@JdbcTest
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Import({TimeRepository.class, ThemeRepository.class, ReservationService.class, ReservationRepository.class})
class ReservationServiceTest {

    @Autowired
    TimeRepository timeRepository;
    @Autowired
    ThemeRepository themeRepository;
    @Autowired
    private ReservationService reservationService;

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
                .isInstanceOf(ConflictException.class);
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
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("현재 날짜가 예약 당일이지만, 이미 지난 시간으로 예약을 생성하면 예외가 발생한다")
    void beforeTimeReservationFail() {
        // given
        LocalDateTime requestTime = LocalDateTime.now();
        LocalDateTime beforeTime = requestTime.minusHours(1L);
        Time time = timeRepository.save(new Time(beforeTime.toLocalTime()));
        Theme theme = themeRepository.save(new Theme("테마명", "설명", "썸네일URL"));

        // when & then
        // TODO: 00:30분 일때 1시간 전은 23:30분 이기 때문에 이전 시간으로 인식하지 않아 테스트코드 실패하는 문제 해결
        assertThatThrownBy(() -> reservationService.createReservation(
                new ReservationRequest("예약", LocalDate.now(), time.getId(), theme.getId())))
                .isInstanceOf(ConflictException.class);
    }
}
