package roomescape.time.application;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.presentation.dto.ReservationRequest;
import roomescape.theme.application.ThemeService;
import roomescape.theme.presentation.dto.ThemeRequest;
import roomescape.theme.presentation.dto.ThemeResponse;
import roomescape.time.presentation.dto.AvailableReservationTimeRequest;
import roomescape.time.presentation.dto.ReservationTimeRequest;
import roomescape.time.presentation.dto.ReservationTimeResponse;

@Transactional
@SpringBootTest
class ReservationTimeServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeService timeService;

    @Autowired
    private ThemeService themeService;
    @Autowired
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("오늘 해당 테마의 예약이 1개 있고, 총 시간이 3개 있으면, 남은 시간은 2개이다.")
    void timeAvailableTest() {
        ThemeRequest theme = ThemeRequest.builder()
                .name("미드나잇")
                .thumbnailImageUrl("https://example.com/theme.png")
                .description("추리 테마")
                .durationTime(LocalTime.of(1, 30))
                .build();
        ThemeResponse themeResponse = themeService.addTheme(theme);
        ReservationTimeResponse timeResponse = timeService.addReservationTime(new ReservationTimeRequest(LocalTime.now()));
        timeService.addReservationTime(new ReservationTimeRequest(LocalTime.now().plusHours(1)));
        timeService.addReservationTime(new ReservationTimeRequest(LocalTime.now().plusHours(2)));
        reservationService.addReservation(new ReservationRequest("포비", LocalDate.now(), timeResponse.id(), themeResponse.id()));
        AvailableReservationTimeRequest availableReservationTimeRequest = new AvailableReservationTimeRequest(
                themeResponse.id(), LocalDate.now());
        Assertions.assertThat(reservationTimeService.getAvailableReservationTime(availableReservationTimeRequest)
                .times()
                .size()
        ).isEqualTo(2);
    }
}
