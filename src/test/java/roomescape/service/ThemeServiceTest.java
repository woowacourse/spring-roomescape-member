package roomescape.service;

import java.util.ArrayList;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    private static final Theme THEME = new Theme(null, new ThemeName("name"), "description", ThemeImageUrl.defaultImageUrl());
    private static final Theme SAVED_THEME = new Theme(1L, new ThemeName("name"), "description", ThemeImageUrl.defaultImageUrl());

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
    }

    @Test
    void 테마를_삭제한다() {
        assertThatCode(() -> themeService.deleteThemeById(SAVED_THEME.getId()))
            .doesNotThrowAnyException();
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
        assertThat(all).hasSize(3);
        assertThat(all).extracting(Theme::getId)
            .anySatisfy(id -> assertThat(id).isEqualTo(1L))
            .anySatisfy(id -> assertThat(id).isEqualTo(2L))
            .anySatisfy(id -> assertThat(id).isEqualTo(3L));
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
    }

    private List<Theme> createTenThemes() {
        List<Theme> themes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            themes.add(new Theme((long) i, new ThemeName("테마" + i), "테마" + i, ThemeImageUrl.defaultImageUrl()));
        }
        return themes;
    }

    ThemeRequest themeRequestDtoFrom(Theme theme) {
        return new ThemeRequest(
            theme.getNameValue(),
            theme.getDescription(),
            theme.getImageUrlValue()
        );
    }
}