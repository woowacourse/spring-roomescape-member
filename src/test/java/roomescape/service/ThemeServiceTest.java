package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Name;
import roomescape.dto.request.ThemeAddRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.repository.theme.ThemeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.InitialDataFixture.THEME_2;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
    @DisplayName("테마를 삭제한다.")
    void deleteTheme() {
        ThemeResponse themeResponse = themeService.addTheme(new ThemeAddRequest("myTestTheme", THEME_2.getDescription(), THEME_2.getThumbnail()));

        int before = themeService.findThemes().size();

        themeService.deleteTheme(themeResponse.toTheme().getId());

        int after = themeService.findThemes().size();

        assertThat(before - after).isEqualTo(1);
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
