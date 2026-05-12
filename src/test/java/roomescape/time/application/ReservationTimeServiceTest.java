package roomescape.time.application;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import roomescape.config.TestTimeConfig;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.application.dto.ReservationCreateCommand;
import roomescape.theme.application.ThemeService;
import roomescape.theme.domain.Theme;
import roomescape.theme.presentation.dto.ThemeRequest;
import roomescape.time.domain.ReservationTime;
import roomescape.time.presentation.dto.AvailableReservationTimeRequest;
import roomescape.time.presentation.dto.ReservationTimeRequest;

@Transactional
@SpringBootTest
@Import(TestTimeConfig.class)
class ReservationTimeServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeService timeService;

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private Clock clock;

    @Test
    @DisplayName("오늘 해당 테마의 예약이 1개 있고, 총 시간이 3개 있으면, 남은 시간은 2개이다.")
    void timeAvailableTest() {
        ThemeRequest theme = ThemeRequest.builder()
                .name("미드나잇")
                .thumbnailImageUrl("https://example.com/theme.png")
                .description("추리 테마")
                .durationTime(LocalTime.now(clock))
                .build();
        Theme savedTheme = themeService.addTheme(theme.toEntity());
        ReservationTime time = timeService.addReservationTime(new ReservationTimeRequest(LocalTime.now(clock)).toEntity());
        timeService.addReservationTime(new ReservationTimeRequest(LocalTime.now(clock).plusHours(1)).toEntity());
        timeService.addReservationTime(new ReservationTimeRequest(LocalTime.now(clock).plusHours(2)).toEntity());
        reservationService.addReservation(new ReservationCreateCommand("포비", LocalDate.now(clock), time.getId(), savedTheme.getId()));
        AvailableReservationTimeRequest availableReservationTimeRequest = new AvailableReservationTimeRequest(
                savedTheme.getId(), LocalDate.now(clock));
        Assertions.assertThat(reservationTimeService.getAvailableReservationTime(availableReservationTimeRequest.toCommand())
                .times()
        ).hasSize(2);
    }
}
