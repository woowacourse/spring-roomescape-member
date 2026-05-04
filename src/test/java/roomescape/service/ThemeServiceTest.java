package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Theme;
import roomescape.dto.theme.ThemeRequestDto;
import roomescape.dto.theme.ThemeResponseDto;
import roomescape.repository.theme.ThemeRepository;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    private static final Theme THEME = new Theme(null, "name", "description", "image-url");
    private static final Theme SAVED_THEME = new Theme(1L, "name", "description", "image-url");
    private static final ThemeRequestDto THEME_REQUEST = ThemeRequestDto.from(THEME);
    private static final ThemeResponseDto THEME_RESPONSE = ThemeResponseDto.from(THEME);

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ThemeService themeService;

    @Test
    void 테마를_추가한다() {
        // given
        when(themeRepository.createTheme(any()))
            .thenReturn(SAVED_THEME);

        // when
        Theme saved = themeService.addTheme(THEME_REQUEST);

        // then
        assertThat(saved).isEqualTo(SAVED_THEME);
    }

    @Test
    void 테마를_삭제한다() {
        assertThatCode(() -> themeService.deleteThemeById(SAVED_THEME.getId()))
            .doesNotThrowAnyException();
    }
}