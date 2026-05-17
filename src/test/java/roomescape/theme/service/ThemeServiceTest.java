package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.config.TestFixture.themeRequest;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.NotFoundException;
import roomescape.theme.entity.Theme;
import roomescape.theme.payload.ThemeRequest;

@Transactional
@SpringBootTest
class ThemeServiceTest {

    private static final String DEFAULT_THEME_NAME = "테마";
    private static final String FIRST_THEME_NAME = "테마1";
    private static final String SECOND_THEME_NAME = "테마2";
    private static final Long NOT_FOUND_ID = 999L;

    @Autowired
    private ThemeService themeService;

    @Test
    void 테마요청을_올바르게_저장하는지_확인하는_테스트() {
        // given
        ThemeRequest request = themeRequest(DEFAULT_THEME_NAME);

        // when
        Theme theme = themeService.save(request);

        // then
        assertThat(theme.getName()).isEqualTo(request.name());
        assertThat(theme.getDescription()).isEqualTo(request.description());
        assertThat(theme.getThumbnailUrl()).isEqualTo(request.thumbnailUrl());
        assertThat(theme.getRuntime()).isEqualTo(Theme.RUNTIME);
    }

    @Test
    void 테마목록을_올바르게_조회하는지_확인하는_테스트() {
        // given
        ThemeRequest request1 = themeRequest(FIRST_THEME_NAME);
        ThemeRequest request2 = themeRequest(SECOND_THEME_NAME);
        Theme theme1 = themeService.save(request1);
        Theme theme2 = themeService.save(request2);

        // when
        List<Theme> themes = themeService.findAll();

        // then
        assertThat(themes).contains(theme1, theme2);
    }

    @Test
    void 테마를_올바르게_삭제하는지_확인하는_테스트() {
        // given
        ThemeRequest request = themeRequest(DEFAULT_THEME_NAME);
        Theme theme = themeService.save(request);

        // when
        themeService.deleteById(theme.getId());

        // then
        List<Theme> themes = themeService.findAll();
        assertThat(themes)
                .extracting(Theme::getId)
                .doesNotContain(theme.getId());
    }

    @Test
    void 없는_테마를_삭제하면_에러를_던진다() {
        // when & then
        assertThatThrownBy(() -> themeService.deleteById(NOT_FOUND_ID))
                .isInstanceOf(NotFoundException.class);
    }

    @Sql("/create_dummies_for_popular_themes.sql")
    @Test
    void 최근_1주_동안_인기있는_테마를_조회한다() {
        // when
        List<Theme> themes = themeService.findPopularThemes(7, 10);

        // then
        assertThat(themes).hasSize(5);
        assertThat(themes)
                .extracting(Theme::getName)
                .containsExactly("테마5", "테마4", "테마3", "테마2", "테마1");
    }

}
