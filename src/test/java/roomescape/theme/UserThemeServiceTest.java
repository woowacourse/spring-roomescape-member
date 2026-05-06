package roomescape.theme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private UserThemeService userThemeService;

    @Test
    void 테마_랭킹을_조회할_수_있다() {
        List<Theme> themes = List.of(
                new Theme(1L, "Theme A", "desc", "thumb"),
                new Theme(2L, "Theme B", "desc", "thumb2")
        );

        when(themeRepository.findRanked(eq("reservationCount"), eq("DESC"), eq(LocalDate.of(2026, 5, 1)),
                eq(LocalDate.of(2026, 5, 31)), eq(10L)))
                .thenReturn(themes);

        List<Theme> result = userThemeService.getThemes("reservationCount", "DESC", LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 31), 10L);

        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(Theme::name)
                .containsExactly("Theme A", "Theme B");
    }

    @Test
    void 전체_테마를_조회할_수_있다() {
        List<Theme> themes = List.of(
                new Theme(1L, "Theme A", "desc", "thumb"),
                new Theme(2L, "Theme B", "desc", "thumb2")
        );

        when(themeRepository.findAll()).thenReturn(themes);

        List<Theme> result = userThemeService.getAllThemes();

        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(Theme::name)
                .containsExactly("Theme A", "Theme B");
    }
}
