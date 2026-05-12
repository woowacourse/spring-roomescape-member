package roomescape.time.application;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
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
class ReservationTimeServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeService timeService;

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ReservationTimeService reservationTimeService;

    private final Clock fixedClock = Clock.fixed(
            Instant.parse("2026-05-12T01:00:00Z"),
            ZoneId.of("Asia/Seoul")
    );

    @Test
    @DisplayName("오늘 해당 테마의 예약이 1개 있고, 총 시간이 3개 있으면, 남은 시간은 2개이다.")
    void timeAvailableTest() {
        ThemeRequest theme = ThemeRequest.builder()
                .name("미드나잇")
                .thumbnailImageUrl("https://example.com/theme.png")
                .description("추리 테마")
                .durationTime(LocalTime.now(fixedClock))
                .build();
        Theme savedTheme = themeService.addTheme(ThemeRequest.toEntity(theme));
        ReservationTime time = timeService.addReservationTime(ReservationTimeRequest.toEntity(new ReservationTimeRequest(LocalTime.now(fixedClock))));
        timeService.addReservationTime(ReservationTimeRequest.toEntity(new ReservationTimeRequest(LocalTime.now(fixedClock).plusHours(1))));
        timeService.addReservationTime(ReservationTimeRequest.toEntity(new ReservationTimeRequest(LocalTime.now(fixedClock).plusHours(2))));
        reservationService.addReservation(new ReservationCreateCommand("포비", LocalDate.now(fixedClock), time.getId(), savedTheme.getId()));
        AvailableReservationTimeRequest availableReservationTimeRequest = new AvailableReservationTimeRequest(
                savedTheme.getId(), LocalDate.now(fixedClock));
        Assertions.assertThat(reservationTimeService.getAvailableReservationTime(availableReservationTimeRequest.toCommand())
                .times()
        ).hasSize(2);
    }
}
