package roomescape.theme.application;

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
import roomescape.theme.application.exception.ThemeInUseException;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.exception.ThemeNotFoundException;
import roomescape.theme.presentation.dto.ThemeRequest;
import roomescape.time.application.ReservationTimeService;
import roomescape.time.application.dto.ReservationTimeCommand;
import roomescape.time.domain.ReservationTime;

@Transactional
@SpringBootTest
@Import(TestTimeConfig.class)
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private Clock clock;

    @Test
    @DisplayName("존재하지 않는 테마를 삭제하려고 하면 에러를 반환한다.")
    void notExistsThemeIdTest() {
        ThemeRequest request = ThemeRequest.builder()
                .name("포비")
                .description("포비가 나와요")
                .thumbnailImageUrl("https://~~~~")
                .durationTime(LocalTime.now(clock))
                .build();

        themeService.addTheme(request.toEntity());
        Assertions.assertThatThrownBy(() -> themeService.deleteTheme(-1L))
                .isInstanceOf(ThemeNotFoundException.class);
    }

    @Test
    @DisplayName("존재하는 테마를 삭제하려고 하면 정상 동작한다.")
    void normalTest() {
        ThemeRequest request = ThemeRequest.builder()
                .name("포비")
                .description("포비가 나와요")
                .thumbnailImageUrl("https://~~~~")
                .durationTime(LocalTime.now(clock))
                .build();
        Theme theme = themeService.addTheme(request.toEntity());
        Assertions.assertThatCode(() -> themeService.deleteTheme(theme.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("예약이 존재하는 테마를 삭제하려고 하면 에러를 반환한다.")
    void themeInUseTest() {
        ReservationTime time = reservationTimeService.addReservationTime(
                ReservationTimeCommand.builder()
                        .startAt(LocalTime.now(clock))
                        .build()
        );

        Theme theme = themeService.addTheme(
                Theme.builder()
                        .name("판타지")
                        .description("판타지래요")
                        .thumbnailImageUrl("https://~~~~")
                        .durationTime(LocalTime.now(clock))
                        .build()
        );

        reservationService.addReservation(
                ReservationCreateCommand.builder()
                        .name("포비")
                        .date(LocalDate.now(clock))
                        .themeId(theme.getId())
                        .timeId(time.getId())
                        .build()
        );

        Assertions.assertThatThrownBy(() -> themeService.deleteTheme(theme.getId()))
                .isInstanceOf(ThemeInUseException.class);
    }
}
