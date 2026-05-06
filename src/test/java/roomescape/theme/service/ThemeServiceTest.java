package roomescape.theme.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.fake.FakeThemeRepository;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.dto.ThemeCreateRequest;
import roomescape.theme.dto.ThemeResponse;

public class ThemeServiceTest {

    private ThemeRepository themeRepository = new FakeThemeRepository();
    private ThemeService themeService = new ThemeService(themeRepository);

    @DisplayName("테마의 정상 추가를 테스트합니다.")
    @Test
    void save_theme_successfully() {
        ThemeCreateRequest createRequestDto = new ThemeCreateRequest("theme name", "theme description", "theme img url");
        ThemeResponse themeResponse = themeService.saveTheme(createRequestDto);

        SoftAssertions.assertSoftly(assertSoftly -> {
            assertSoftly.assertThat(themeResponse.id()).isEqualTo(1L);
            assertSoftly.assertThat(themeResponse.name()).isEqualTo("theme name");
            assertSoftly.assertThat(themeResponse.description()).isEqualTo("theme description");
            assertSoftly.assertThat(themeResponse.thumbnailImgUrl()).isEqualTo("theme img url");
        });
    }

    @DisplayName("중복된 테마 추가 시 예외 발생을 테스트합니다.")
    @Test
    void save_duplicated_theme_exception() {
        ThemeCreateRequest createRequestDto = new ThemeCreateRequest("theme name", "theme description", "theme img url");
        themeService.saveTheme(createRequestDto);

        assertThatThrownBy(() -> themeService.saveTheme(createRequestDto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테마의 삭제를 테스트합니다.")
    @Test
    void delete_theme() {
        ThemeCreateRequest createRequestDto = new ThemeCreateRequest("theme name", "theme description", "theme img url");
        themeService.saveTheme(createRequestDto);

        assertThat(themeService.delete(1L)).isEqualTo(1);
    }

    @DisplayName("테마의 전체 조회를 테스트합니다.")
    @Test
    void find_all_themes() {
        ThemeCreateRequest createRequestDto1 = new ThemeCreateRequest("theme name1", "theme description1", "theme img url1");
        ThemeCreateRequest createRequestDto2 = new ThemeCreateRequest("theme name2", "theme description2", "theme img url2");
        ThemeCreateRequest createRequestDto3 = new ThemeCreateRequest("theme name3", "theme description3", "theme img url3");

        themeService.saveTheme(createRequestDto1);
        themeService.saveTheme(createRequestDto2);
        themeService.saveTheme(createRequestDto3);

        List<ThemeResponse> themeResponses = themeService.findAllThemes();

        SoftAssertions.assertSoftly(assertSoftly -> {
            assertSoftly.assertThat(themeResponses.size()).isEqualTo(3);
            assertSoftly.assertThat(themeResponses).containsExactly(
                    new ThemeResponse(1L, "theme name1", "theme description1", "theme img url1"),
                    new ThemeResponse(2L, "theme name2", "theme description2", "theme img url2"),
                    new ThemeResponse(3L, "theme name3", "theme description3", "theme img url3")
            );
        });
    }
}
