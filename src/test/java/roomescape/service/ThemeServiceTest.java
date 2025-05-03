package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.Theme;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ThemeServiceTest {
    private final ThemeService themeService;

    @Autowired
    public ThemeServiceTest(ThemeService themeService) {
        this.themeService = themeService;
    }

    @Test
    void saveTest() {
        // given
        Theme theme = new Theme(1L, "Test Theme", "Test Description", "image.jpg");

        // when
        themeService.save(theme);

        // then
        List<Theme> themes = themeService.read();
        Assertions.assertAll(
            () -> assertThat(themes).hasSize(1),
            () -> assertThat(themes.getFirst().equals(theme)).isTrue()
        );
    }

    @Test
    void readTest() {
        // given
        Theme theme1 = new Theme(1L, "Test Theme 1", "Test Description 1", "image1.jpg");
        Theme theme2 = new Theme(2L, "Test Theme 2", "Test Description 2", "image2.jpg");
        themeService.save(theme1);
        themeService.save(theme2);

        // when
        List<Theme> themes = themeService.read();

        // then
        Assertions.assertAll(
            () -> assertThat(themes).hasSize(2),
            () -> assertThat(themes.get(0)).isEqualTo(theme1),
            () -> assertThat(themes.get(1)).isEqualTo(theme2)
        );
    }

    @Test
    void deleteTest() {
        // given
        Theme theme = new Theme(1L, "Test Theme", "Test Description", "image.jpg");
        themeService.save(theme);

        // when
        themeService.delete(theme.getId());

        // then
        List<Theme> themes = themeService.read();
        Assertions.assertAll(
            () -> assertThat(themes).hasSize(0)
        );
    }

    @Test
    void readListsTest() {
        // given
        Theme theme1 = new Theme(1L, "Test Theme 1", "Test Description 1", "image1.jpg");
        Theme theme2 = new Theme(2L, "Test Theme 2", "Test Description 2", "image2.jpg");
        themeService.save(theme1);
        themeService.save(theme2);

        // when
        List<Theme> themesAsc = themeService.readLists("popular_asc", 10L);
        List<Theme> themesDesc = themeService.readLists("popular_desc", 10L);

        // then
        Assertions.assertAll(
                () -> assertThat(themesAsc).hasSize(0),
                () -> assertThat(themesDesc).hasSize(0)
        );
    }
}
