package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.dto.ThemeRequest;
import roomescape.model.Theme;
import roomescape.repository.ThemeRepository;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {
    @InjectMocks
    private ThemeService themeService;
    @Mock
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("테마 추가 테스트")
    void test1() {
        ThemeRequest themeRequest = new ThemeRequest("테마1", "테마설명1", "썸네일링크1");
        Theme theme = new Theme(1L, "테마1", "테마설명1", "썸네일링크1");

        when(themeRepository.addTheme(any(Theme.class))).thenReturn(theme);

        // then
        assertThat(themeService.addTheme(themeRequest)).isEqualTo(theme);
    }

    @Test
    @DisplayName("전체 테마 조회 테스트")
    void test2() {
        Theme theme = new Theme(1L, "테마1", "테마설명1", "썸네일링크1");
        Theme theme2 = new Theme(2L, "테마2", "테마설명2", "썸네일링크2");

        when(themeRepository.getAllTheme()).thenReturn(List.of(theme, theme2));

        assertThat(themeService.getAllThemes()).isEqualTo(List.of(theme, theme2));
    }

    @Test
    @DisplayName("ID 테마 조회 테스트")
    void test3() {

    }
}
