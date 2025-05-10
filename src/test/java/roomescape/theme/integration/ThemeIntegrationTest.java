package roomescape.theme.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.theme.dto.request.ThemeRequest.ThemeCreateRequest;
import roomescape.theme.dto.response.ThemeResponse.ThemeCreateResponse;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.ThemeService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeIntegrationTest {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("테마를 DB에 저장한다.")
    void createTheme() {
        // given
        String name = "미소";
        String description = "미소 테마";
        String thumbnail = "https://miso.com";
        ThemeCreateRequest request = new ThemeCreateRequest(
                name,
                description,
                thumbnail
        );

        // when
        ThemeCreateResponse response = themeService.createTheme(request);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo(name);
        assertThat(response.thumbnail()).isEqualTo(thumbnail);
        assertThat(response.thumbnail()).isEqualTo(thumbnail);
    }

    @Test
    @DisplayName("모든 테마를 DB에서 조회한다.")
    void getAllThemes() {
        // given
        String name = "미소";
        String description = "미소 테마";
        String thumbnail = "https://miso.com";
        ThemeCreateRequest request = new ThemeCreateRequest(
                name,
                description,
                thumbnail
        );

        // when
        ThemeCreateResponse response = themeService.createTheme(request);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo(name);
        assertThat(response.description()).isEqualTo(description);
        assertThat(response.thumbnail()).isEqualTo(thumbnail);
    }

    @Test
    @DisplayName("인기 있는 테마 몇가지를 조회한다.")
    void getPopularThemes() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("테마를 DB에서 삭제한다.")
    void deleteTheme() {
        // given
        long id = 1L;
        Theme theme = new Theme(
                id,
                "미소",
                "미소 테마",
                "https://miso.com"
        );
        themeRepository.save(theme);

        // when
        themeService.deleteTheme(id);

        // then
        List<Theme> themes = themeRepository.findAll();
        assertThat(themes.size()).isEqualTo(0);
    }
}
