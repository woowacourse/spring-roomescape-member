package roomescape.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Theme;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeServiceTest {
    @Autowired
    private ThemeService themeService;

    @Test
    void 테마를_저장하고_조회한다() {
        Theme savedTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        List<Theme> themes = themeService.findAll();
        assertThat(themes.getFirst().getId()).isEqualTo(savedTheme.getId());
    }

    @Test
    void 테마를_저장하고_삭제한다() {
        Theme savedTheme = themeService.save(new Theme("공포", "무서움", "https://roomescape.com"));
        themeService.deleteById(savedTheme.getId());
        assertThat(themeService.findAll()).hasSize(0);
    }
}
