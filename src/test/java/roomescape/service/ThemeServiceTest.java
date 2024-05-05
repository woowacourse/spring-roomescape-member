package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.request.ThemeAddRequest;
import roomescape.dto.response.ThemeResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.InitialDataFixture.THEME_2;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/initial_test_data.sql")
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Test
    @DisplayName("테마를 추가한다.")
    void addTheme() {
        ThemeAddRequest themeAddRequest = new ThemeAddRequest("test", "test", "test");

        ThemeResponse themeResponse = themeService.addTheme(themeAddRequest);

        assertThat(themeResponse.id()).isNotNull();
    }

    @Test
    @DisplayName("테마를 조회한다.")
    void findTheme() {
        List<ThemeResponse> themes = themeService.findThemes();

        assertThat(themes).hasSize(2);
    }

    @Test
    @DisplayName("테마를 삭제한다.")
    void deleteTheme() {
        themeService.deleteTheme(THEME_2.getId());

        List<ThemeResponse> themes = themeService.findThemes();

        assertThat(themes).hasSize(1);
    }

    @Test
    @DisplayName("중복된 테마를 저장하려고 하면 예외가 발생한다.")
    void saveDuplicatedTime() {
        ThemeAddRequest themeAddRequest = new ThemeAddRequest("test", "test", "test");

        themeService.addTheme(themeAddRequest);

        assertThatThrownBy(() -> themeService.addTheme(themeAddRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
