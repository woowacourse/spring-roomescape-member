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
import roomescape.theme.entity.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.payload.ThemeRequest;

@Transactional
@SpringBootTest
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Test
    void 테마요청을_올바르게_저장하는지_확인하는_테스트() {
        ThemeRequest request = themeRequest("테마");

        Theme theme = themeService.save(request);

        assertThat(theme.getName()).isEqualTo(request.name());
        assertThat(theme.getDescription()).isEqualTo(request.description());
        assertThat(theme.getThumbnailUrl()).isEqualTo(request.thumbnailUrl());
        assertThat(theme.getRuntime()).isEqualTo(Theme.RUNTIME);
    }

    @Test
    void 테마목록을_올바르게_조회하는지_확인하는_테스트() {
        ThemeRequest request1 = themeRequest("테마1");
        ThemeRequest request2 = themeRequest("테마2");
        Theme theme1 = themeService.save(request1);
        Theme theme2 = themeService.save(request2);

        List<Theme> themes = themeService.findAll();

        assertThat(themes).contains(theme1, theme2);
    }

    @Test
    void 테마를_올바르게_삭제하는지_확인하는_테스트() {
        ThemeRequest request = themeRequest("테마");
        Theme theme = themeService.save(request);

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
