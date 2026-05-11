package roomescape.admin.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.doamin.Theme;
import roomescape.theme.repository.ThemeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminThemeServiceTest {

    @Autowired
    private AdminThemeService adminThemeService;

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("관리자는 테마를 저장할 수 있다.")
    @Test
    void saveTheme() {
        Theme savedTheme = adminThemeService.saveTheme(
                "감옥 탈출",
                "철통 보안 감옥에서 탈출하라!",
                "https://image.test/prison.png"
        );

        assertThat(savedTheme.getId()).isNotNull();

        Theme foundTheme = themeRepository.findById(savedTheme.getId())
                .orElseThrow();

        assertThat(foundTheme.getName()).isEqualTo("감옥 탈출");
        assertThat(foundTheme.getDescription()).isEqualTo("철통 보안 감옥에서 탈출하라!");
        assertThat(foundTheme.getThumbnailUrl())
                .isEqualTo("https://image.test/prison.png");
    }

    @DisplayName("관리자는 테마를 삭제할 수 있다.")
    @Test
    void deleteTheme() {
        // test-data.sql 기준 theme_id=4 존재
        adminThemeService.deleteTheme(4L);

        assertThat(themeRepository.findById(4L)).isEmpty();
    }
}