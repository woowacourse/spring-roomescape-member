package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.theme.entity.Theme;
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
        assertThat(theme.getRuntime()).isEqualTo(Duration.ofHours(1));
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
        assertThat(themes).doesNotContain(theme);
    }

}
