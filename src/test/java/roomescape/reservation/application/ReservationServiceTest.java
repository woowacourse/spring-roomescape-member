package roomescape.reservation.application;

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
import roomescape.reservation.application.dto.ReservationCreateCommand;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.application.exception.ReservationInUseException;
import roomescape.theme.application.ThemeService;
import roomescape.theme.domain.Theme;
import roomescape.time.application.ReservationTimeService;
import roomescape.time.application.dto.ReservationTimeCommand;
import roomescape.time.domain.ReservationTime;

@Transactional
@SpringBootTest
@Import(TestTimeConfig.class)
class ReservationServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ThemeService themeService;

    @Autowired
    private Clock clock;

    @Test
    @DisplayName("예약이 취소되면 다음 예약을 할 수 있다.")
    void canReservationAfterCancel() {
        ReservationTime time = reservationTimeService.addReservationTime(ReservationTimeCommand.builder()
                .startAt(LocalTime.now(clock))
                .build()
        );
        Theme theme = themeService.addTheme(Theme.builder()
                .name("포비")
                .durationTime(LocalTime.now(clock))
                .thumbnailImageUrl("https://~~~")
                .description("포비가 나와요")
                .build()
        );
        Reservation reservation = reservationService.addReservation(ReservationCreateCommand.builder()
                .name("리사")
                .date(LocalDate.now(clock))
                .timeId(time.getId())
                .themeId(theme.getId())
                .build()
        );
        Assertions.assertThatThrownBy(() -> reservationService.addReservation(ReservationCreateCommand.builder()
                        .name("워니")
                        .date(LocalDate.now(clock))
                        .timeId(time.getId())
                        .themeId(theme.getId())
                .build()
        )).isInstanceOf(ReservationInUseException.class);
        reservationService.cancelReservation(reservation.getId(), reservation.getName());
        Assertions.assertThatCode(() -> reservationService.addReservation(ReservationCreateCommand.builder()
                .name("워니")
                .date(LocalDate.now(clock))
                .timeId(time.getId())
                .themeId(theme.getId())
                .build()
        )).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("자신의 ID를 시간 변경 없이 그대로 수정해도 수정된다.")
    void canChangeTest() {
        ReservationTime time = reservationTimeService.addReservationTime(ReservationTimeCommand.builder()
                .startAt(LocalTime.now(clock))
                .build()
        );
        Theme theme = themeService.addTheme(Theme.builder()
                .name("포비")
                .durationTime(LocalTime.now(clock))
                .thumbnailImageUrl("https://~~~")
                .description("포비가 나와요")
                .build()
        );
        Reservation reservation = reservationService.addReservation(ReservationCreateCommand.builder()
                .name("리사")
                .date(LocalDate.now(clock))
                .timeId(time.getId())
                .themeId(theme.getId())
                .build()
        );
        Assertions.assertThatCode(() -> reservation.changeTime(
                reservation.getDate(), reservation.getTime(), reservation.getTheme()))
                .doesNotThrowAnyException();
    }
}
