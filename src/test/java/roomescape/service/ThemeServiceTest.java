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
import roomescape.dto.request.ThemeCreateRequest;
import roomescape.dto.response.ThemeResponse;

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
    void createThemeTest() {
        // given
        ThemeCreateRequest request = new ThemeCreateRequest("Test Theme", "Test Description", "image.jpg");

        // when
        themeService.createTheme(request);

        // then
        List<ThemeResponse> themes = themeService.findAll();
        Assertions.assertAll(
                () -> assertThat(themes).hasSize(1),
                () -> assertThat(themes.getFirst().name().equals(request.name())).isTrue(),
                () -> assertThat(themes.getFirst().description().equals(request.description())).isTrue(),
                () -> assertThat(themes.getFirst().thumbnail().equals(request.thumbnail())).isTrue()
        );
    }

    @Test
    void findAllTest() {
        // given
        ThemeCreateRequest request1 = new ThemeCreateRequest("Test Theme 1", "Test Description 1", "image1.jpg");
        ThemeCreateRequest request2 = new ThemeCreateRequest("Test Theme 2", "Test Description 2", "image2.jpg");
        themeService.createTheme(request1);
        themeService.createTheme(request2);

        // when
        List<ThemeResponse> themes = themeService.findAll();

        // then
        Assertions.assertAll(
                () -> assertThat(themes).hasSize(2),
                () -> assertThat(themes.get(0).name().equals(request1.name())).isTrue(),
                () -> assertThat(themes.get(0).description().equals(request1.description())).isTrue(),
                () -> assertThat(themes.get(0).thumbnail().equals(request1.thumbnail())).isTrue(),
                () -> assertThat(themes.get(1).name().equals(request2.name())).isTrue(),
                () -> assertThat(themes.get(1).description().equals(request2.description())).isTrue(),
                () -> assertThat(themes.get(1).thumbnail().equals(request2.thumbnail())).isTrue()
        );
    }

    @Test
    void deleteThemeByIdTest() {
        // given
        ThemeCreateRequest request = new ThemeCreateRequest("Test Theme", "Test Description", "image.jpg");
        ThemeResponse savedTheme = themeService.createTheme(request);

        // when
        themeService.deleteThemeById(savedTheme.id());

        // then
        List<ThemeResponse> themes = themeService.findAll();
        Assertions.assertAll(
                () -> assertThat(themes).hasSize(0)
        );
    }

    @Test
    void findAllListsTest() {
        // given
        ThemeCreateRequest request1 = new ThemeCreateRequest("Test Theme 1", "Test Description 1", "image1.jpg");
        ThemeCreateRequest request2 = new ThemeCreateRequest("Test Theme 2", "Test Description 2", "image2.jpg");
        themeService.createTheme(request1);
        themeService.createTheme(request2);

        // when
        List<ThemeResponse> themesAsc = themeService.findLimitedThemesByPopularDesc("popular_asc", 10L);
        List<ThemeResponse> themesDesc = themeService.findLimitedThemesByPopularDesc("popular_desc", 10L);

        // then
        Assertions.assertAll(
                () -> assertThat(themesAsc).hasSize(0),
                () -> assertThat(themesDesc).hasSize(0)
        );
    }
}
