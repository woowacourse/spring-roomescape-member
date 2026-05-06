package roomescape.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.theme.entity.Theme;
import roomescape.theme.exception.ThemeNotFoundException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class JdbcThemeRepositoryTest {

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    void 테마를_저장하는_테스트() {
        String name = "테마";
        String description = "테마 설명";
        String thumbnailUrl = "https://example.com/theme.png";

        Theme theme = themeRepository.save(name, description, thumbnailUrl);

        assertThat(theme.getId()).isPositive();
        assertThat(theme.getName()).isEqualTo(name);
        assertThat(theme.getDescription()).isEqualTo(description);
        assertThat(theme.getThumbnailUrl()).isEqualTo(thumbnailUrl);
        assertThat(theme.getRuntime()).isEqualTo(Duration.ofHours(1));
    }

    @Test
    void 테마를_조회하는_테스트() {
        String name = "테마";
        String description = "테마 설명";
        String thumbnailUrl = "https://example.com/theme.png";

        Theme theme = themeRepository.save(name, description, thumbnailUrl);

        Theme foundTheme = themeRepository.findById(theme.getId())
                .orElseThrow(() -> new ThemeNotFoundException(theme.getId()));

        assertThat(foundTheme.getId()).isEqualTo(theme.getId());
        assertThat(foundTheme.getName()).isEqualTo(name);
        assertThat(foundTheme.getDescription()).isEqualTo(description);
        assertThat(foundTheme.getThumbnailUrl()).isEqualTo(thumbnailUrl);
        assertThat(foundTheme.getRuntime()).isEqualTo(Duration.ofHours(1));
    }

    @Test
    void 모든_테마를_조회하는_테스트() {
        Theme theme1 = themeRepository.save("테마1", "테마 설명1", "https://example.com/theme1.png");
        Theme theme2 = themeRepository.save("테마2", "테마 설명2", "https://example.com/theme2.png");

        List<Theme> themes = themeRepository.findAll();

        assertThat(themes).contains(theme1, theme2);
    }

    @Test
    void 테마를_삭제하는_테스트() {
        Theme theme = themeRepository.save("테마", "테마 설명", "https://example.com/theme.png");
        themeRepository.deleteById(theme.getId());

        List<Theme> themes = themeRepository.findAll();
        assertThat(themes).doesNotContain(theme);
    }

}
