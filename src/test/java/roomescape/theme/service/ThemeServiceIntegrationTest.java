package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.SaveThemeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = BEFORE_TEST_METHOD)
class ThemeServiceIntegrationTest {

  @Autowired
  private ThemeService themeService;

  @DisplayName("전체 테마 정보를 조회한다.")
  @Test
  void getThemesTest() {
    // When
    final List<Theme> themes = themeService.getThemes();

    // Then
    assertThat(themes).hasSize(15);
  }

  @DisplayName("테마 정보를 저장한다.")
  @Test
  void saveThemeTest() {
    // Given
    final String name = "켈리의 두근두근";
    final String description = "켈리와의 두근두근 데이트";
    final String thumbnail = "켈리 사진";
    final SaveThemeRequest saveThemeRequest = new SaveThemeRequest(name, description, thumbnail);

    // When
    final Theme theme = themeService.saveTheme(saveThemeRequest);

    // Then
    final List<Theme> themes = themeService.getThemes();
    Assertions.assertAll(
        () -> assertThat(themes).hasSize(16),
        () -> assertThat(theme.getId()).isEqualTo(16L),
        () -> assertThat(theme.getName().getValue()).isEqualTo(name),
        () -> assertThat(theme.getDescription().getValue()).isEqualTo(description),
        () -> assertThat(theme.getThumbnail()).isEqualTo(thumbnail)
    );
  }

  @DisplayName("테마 정보를 삭제한다.")
  @Test
  void deleteThemeTest() {
    // When
    themeService.deleteTheme(7L);

    // Then
    final List<Theme> themes = themeService.getThemes();
    assertThat(themes).hasSize(14);
  }
}
