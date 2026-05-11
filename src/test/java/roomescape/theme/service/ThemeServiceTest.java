package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.theme.entity.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.payload.ThemeRequest;

@SpringBootTest
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Test
    void 테마요청을_올바르게_저장하는지_확인하는_테스트() {
        ThemeRequest themeRequest = new ThemeRequest("테마", "테마 설명", "https://example.com/theme.png");

        Theme theme = themeService.save(themeRequest);

        assertThat(theme.getName()).isEqualTo(themeRequest.name());
        assertThat(theme.getDescription()).isEqualTo(themeRequest.description());
        assertThat(theme.getThumbnailUrl()).isEqualTo(themeRequest.thumbnailUrl());
        assertThat(theme.getRuntime()).isEqualTo(Theme.RUNTIME);
    }

    @Test
    void 테마목록을_올바르게_조회하는지_확인하는_테스트() {
        ThemeRequest themeRequest1 = new ThemeRequest("테마1", "테마 설명1", "https://example.com/theme1.png");
        ThemeRequest themeRequest2 = new ThemeRequest("테마2", "테마 설명2", "https://example.com/theme2.png");
        Theme theme1 = themeService.save(themeRequest1);
        Theme theme2 = themeService.save(themeRequest2);

        List<Theme> themes = themeService.findAll();

        assertThat(themes).contains(theme1, theme2);
    }

    @Test
    void 테마를_올바르게_삭제하는지_확인하는_테스트() {
        ThemeRequest themeRequest = new ThemeRequest("테마", "테마 설명", "https://example.com/theme.png");
        Theme theme = themeService.save(themeRequest);

        themeService.deleteById(theme.getId());

        List<Theme> themes = themeService.findAll();
        assertThat(themes)
                .extracting(Theme::getId)
                .doesNotContain(theme.getId());
    }

    @Test
    void 없는_테마를_삭제하면_에러를_던진다() {
        assertThatThrownBy(() -> themeService.deleteById(999L))
                .isInstanceOf(ThemeNotFoundException.class);
    }

    @Sql("/create_dummies_for_popular_themes.sql")
    @Test
    void 최근_1주_동안_인기있는_테마를_조회한다() {
        List<Theme> themes = themeService.findPopularThemes(7, 10);

        assertThat(themes).hasSize(5);
        assertThat(themes)
                .extracting(Theme::getName)
                .containsExactly("테마5", "테마4", "테마3", "테마2", "테마1");
    }

}
