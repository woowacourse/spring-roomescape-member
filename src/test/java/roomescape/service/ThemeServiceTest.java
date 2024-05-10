package roomescape.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.theme.dto.ThemeResponse;
import roomescape.domain.theme.service.ThemeService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Test
    void findAllThemes() {
        List<ThemeResponse> allThemes = themeService.findAllThemes();

        assertThat(allThemes).containsExactly(
                new ThemeResponse(1L, "테마1", "설명", "썸네일"),
                new ThemeResponse(2L, "테마2", "설명", "썸네일")
        );
    }

    @Test
    void deleteById() {
        themeService.deleteThemeById(1L);

        List<ThemeResponse> allThemes = themeService.findAllThemes();
        assertThat(allThemes).doesNotContain(new ThemeResponse(1L, "테마1", "설명", "썸네일"));
    }
}
