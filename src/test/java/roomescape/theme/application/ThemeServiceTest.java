package roomescape.theme.application;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.exception.ThemeNotFoundException;
import roomescape.theme.presentation.dto.ThemeRequest;

@Transactional
@SpringBootTest
class ThemeServiceTest {

    @Autowired
    private ThemeService service;

    private final Clock fixedClock = Clock.fixed(
            Instant.parse("2026-05-12T01:00:00Z"),
            ZoneId.of("Asia/Seoul")
    );

    @Test
    @DisplayName("존재하지 않는 테마를 삭제하려고 하면 에러를 반환한다.")
    void notExistsThemeIdTest() {
        ThemeRequest request = ThemeRequest.builder()
                .name("포비")
                .description("포비가 나와요")
                .thumbnailImageUrl("https://~~~~")
                .durationTime(LocalTime.now(fixedClock))
                .build();

        service.addTheme(request.toEntity());
        Assertions.assertThatThrownBy(() -> service.deleteTheme(-1L))
                .isInstanceOf(ThemeNotFoundException.class);
    }

    @Test
    @DisplayName("존재하는 테마를 삭제하려고 하면 정상 동작한다.")
    void normalTest() {
        ThemeRequest request = ThemeRequest.builder()
                .name("포비")
                .description("포비가 나와요")
                .thumbnailImageUrl("https://~~~~")
                .durationTime(LocalTime.now(fixedClock))
                .build();
        Theme theme = service.addTheme(request.toEntity());
        Assertions.assertThatCode(() -> service.deleteTheme(theme.getId()))
                .doesNotThrowAnyException();
    }
}
