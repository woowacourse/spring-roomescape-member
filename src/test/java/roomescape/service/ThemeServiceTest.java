package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Theme;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;
import roomescape.dto.theme.ThemeRequest;
import roomescape.repository.theme.ThemeRepository;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    private static final Theme THEME = new Theme(null, new ThemeName("name"), "description",
        ThemeImageUrl.defaultImageUrl());
    private static final Theme SAVED_THEME = new Theme(1L, new ThemeName("name"), "description",
        ThemeImageUrl.defaultImageUrl());

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
        Theme saved = themeService.addTheme(themeRequestDtoFrom(THEME));

        // then
        assertThat(saved).isEqualTo(SAVED_THEME);

        verify(themeRepository, times(1)).createTheme(any());
        verifyNoMoreInteractions(themeRepository);
    }

    @Test
    void 테마를_삭제한다() {
        // when & then
        assertThatCode(() -> themeService.deleteThemeById(SAVED_THEME.getId()))
            .doesNotThrowAnyException();

        verify(themeRepository, times(1)).deleteById(SAVED_THEME.getId());
        verifyNoMoreInteractions(themeRepository);
    }

    @Test
    void 모든_테마를_조회한다() {
        // given
        List<Theme> themes = List.of(THEME.withId(1L), THEME.withId(2L), THEME.withId(3L));

        when(themeRepository.findAll())
            .thenReturn(themes);

        // when
        List<Theme> all = themeService.getThemes();

        // then
        assertThat(all).containsExactlyElementsOf(themes);

        verify(themeRepository, times(1)).findAll();
        verifyNoMoreInteractions(themeRepository);
    }

    @Test
    void 최근_1주일간_예약이_많은_테마_상위_10개를_조회할_수_있다() {
        // given
        List<Theme> tenPopularThemesOrderByRank = createTenThemes();

        when(themeRepository.findWeekPopularThemesOrderByRank(10))
            .thenReturn(tenPopularThemesOrderByRank);

        // when
        List<Theme> themes = themeService.findWeekPopularThemesOrderByRank(10);

        // then
        assertThat(themes).containsExactlyElementsOf(tenPopularThemesOrderByRank);

        verify(themeRepository, times(1)).findWeekPopularThemesOrderByRank(10);
        verifyNoMoreInteractions(themeRepository);
    }

    private List<Theme> createTenThemes() {
        List<Theme> themes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            themes.add(new Theme((long) i, new ThemeName("테마" + i), "테마" + i, ThemeImageUrl.defaultImageUrl()));
        }
        return themes;
    }

    private ThemeRequest themeRequestDtoFrom(Theme theme) {
        return new ThemeRequest(
            theme.getNameValue(),
            theme.getDescription(),
            theme.getImageUrlValue()
        );
    }
}
