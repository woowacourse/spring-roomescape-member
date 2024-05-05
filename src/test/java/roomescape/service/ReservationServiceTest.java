package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.global.exception.model.ConflictException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.TimeRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        Time time = timeRepository.insert(new Time(LocalTime.of(12, 30)));
        Theme theme = themeRepository.insert(new Theme("테마명", "설명", "썸네일URL"));

        // when & then
        reservationService.addReservation(
                new ReservationRequest("예약", LocalDate.now().plusDays(1L), time.getId(), theme.getId()));

        assertThatThrownBy(() -> reservationService.addReservation(
                new ReservationRequest("예약", LocalDate.now().plusDays(1L), time.getId(), theme.getId())))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("이미 지난 날짜로 예약을 생성하면 예외가 발생한다")
    void beforeDateReservationFail() {
        // given
        Time time = timeRepository.insert(new Time(LocalTime.of(12, 30)));
        Theme theme = themeRepository.insert(new Theme("테마명", "설명", "썸네일URL"));
        LocalDate beforeDate = LocalDate.now().minusDays(1L);

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(
                new ReservationRequest("예약", beforeDate, time.getId(), theme.getId())))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("현재 날짜가 예약 당일이지만, 이미 지난 시간으로 예약을 생성하면 예외가 발생한다")
    void beforeTimeReservationFail() {
        // given
        LocalDateTime beforeTime = LocalDateTime.now().minusHours(1L);
        Time time = timeRepository.insert(new Time(beforeTime.toLocalTime()));
        Theme theme = themeRepository.insert(new Theme("테마명", "설명", "썸네일URL"));

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(
                new ReservationRequest("예약", beforeTime.toLocalDate(), time.getId(), theme.getId())))
                .isInstanceOf(ConflictException.class);
    }
}
