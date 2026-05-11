package roomescape.reservation.application;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.reservation.application.dto.ReservationCreateCommand;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.exception.ReservationInUseException;
import roomescape.theme.application.ThemeService;
import roomescape.theme.domain.Theme;
import roomescape.time.application.ReservationTimeService;
import roomescape.time.domain.ReservationTime;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ThemeService themeService;

    @Test
    @DisplayName("예약이 취소되면 다음 예약을 할 수 있다.")
    void canReservationAfterCancel() {
        ReservationTime time = reservationTimeService.addReservationTime(ReservationTime.builder()
                .startAt(LocalTime.now())
                .build()
        );
        Theme theme = themeService.addTheme(Theme.builder()
                .name("포비")
                .durationTime(LocalTime.of(1, 0))
                .thumbnailImageUrl("https://~~~")
                .description("포비가 나와요")
                .build()
        );
        Reservation reservation = reservationService.addReservation(ReservationCreateCommand.builder()
                .name("리사")
                .date(LocalDate.now())
                .timeId(time.getId())
                .themeId(theme.getId())
                .build()
        );
        Assertions.assertThatThrownBy(() -> reservationService.addReservation(ReservationCreateCommand.builder()
                        .name("워니")
                        .date(LocalDate.now())
                        .timeId(time.getId())
                        .themeId(theme.getId())
                .build()
        )).isInstanceOf(ReservationInUseException.class);
        reservationService.cancelReservation(reservation.getId(), reservation.getName());
        Assertions.assertThatCode(() -> reservationService.addReservation(ReservationCreateCommand.builder()
                .name("워니")
                .date(LocalDate.now())
                .timeId(time.getId())
                .themeId(theme.getId())
                .build()
        )).doesNotThrowAnyException();
    }
}
