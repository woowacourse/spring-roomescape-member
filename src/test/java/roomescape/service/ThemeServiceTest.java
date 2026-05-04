package roomescape.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Theme;
import roomescape.dto.theme.ThemeRequestDto;
import roomescape.repository.theme.ThemeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    private static final Theme THEME = new Theme(null, "name", "description", "image-url");
    private static final Theme SAVED_THEME = new Theme(1L, "name", "description", "image-url");
    private static final ThemeRequestDto THEME_REQUEST = ThemeRequestDto.from(THEME);

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

    @Test
    void 모든_테마를_조회한다() {
        List<Theme> themes = List.of(THEME.withId(1L), THEME.withId(2L), THEME.withId(3L));

        // given
        when(themeRepository.findAll())
                .thenReturn(themes);

        // when
        List<Theme> all = themeService.findAll();

        // then
        assertThat(all).hasSize(3);
        assertThat(all).extracting(Theme::getId)
                .anySatisfy(id -> assertThat(id).isEqualTo(1L))
                .anySatisfy(id -> assertThat(id).isEqualTo(2L))
                .anySatisfy(id -> assertThat(id).isEqualTo(3L));
    }
}