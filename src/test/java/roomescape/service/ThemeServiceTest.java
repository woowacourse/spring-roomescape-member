package roomescape.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.ThemeResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void findAllThemes() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "곰세마리", "공포", "푸우");

        List<ThemeResponse> allThemes = themeService.findAllThemes();

        assertThat(allThemes).containsExactly(new ThemeResponse(1L, "곰세마리", "공포", "푸우"));
    }

    @Test
    void deleteById() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "곰세마리", "공포", "푸우");

        themeService.deleteThemeById(1L);

        List<ThemeResponse> allThemes = themeService.findAllThemes();
        assertThat(allThemes).doesNotContain(new ThemeResponse(1L, "곰세마리", "공포", "푸우"));
    }
}
