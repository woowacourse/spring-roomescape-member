package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.global.exception.ApplicationException;
import roomescape.repository.ThemeRepository;
import roomescape.repository.TimeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ReservationServiceSpringBootTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private ThemeRepository themeRepository;

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
                .isInstanceOf(ApplicationException.class);
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
                .isInstanceOf(ApplicationException.class);
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
        ).isInstanceOf(ApplicationException.class);
    }
}
