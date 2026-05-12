package roomescape.theme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.repository.ThemeRepository;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    private final Clock fixedClock = Clock.fixed(
            Instant.parse("2026-06-08T00:00:00Z"),
            ZoneId.of("Asia/Seoul")
    );

    @Test
    void 인기_테마_조회() {
        ThemeService themeService = new ThemeService(themeRepository, fixedClock);
        Theme theme = new Theme(5L, "초보자 방", "방탈출이 처음이신 분들을 위한 입문 테마.",
                "https://example.com/themes/beginner.jpg");
        given(themeRepository.findPopularThemes(LocalDate.of(2026, 6,
                1), LocalDate.of(2026, 6, 7)))
                .willReturn(List.of(theme));

        List<ThemeResponse> themes = themeService.readPopularThemes();

        assertThat(themes).hasSize(1);
        assertThat(themes.get(0).id()).isEqualTo(5L);
    }
}
