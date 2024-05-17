package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.SaveThemeRequest;
import roomescape.theme.repository.ThemeRepository;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

  @InjectMocks
  private ThemeService themeService;
  @Mock
  private ThemeRepository themeRepository;

  @DisplayName("전체 테마 정보를 조회한다.")
  @Test
  void getThemesTest() {
    // Given
    final List<Theme> themes = List.of(
        Theme.of(1L, "켈리의 하루", "켈켈켈", "켈리 사진"),
        Theme.of(2L, "테바의 하루", "테테테", "테바 사진")
    );

    given(themeRepository.findAll()).willReturn(themes);

    // When
    final List<Theme> findThemes = themeService.getThemes();

    // Then
    assertThat(findThemes).hasSize(2);
  }

  @DisplayName("예약 시간 정보를 저장한다.")
  @Test
  void saveThemeTest() {
    // Given
    final String name = "켈리의 하루";
    final String description = "켈켈켈";
    final String thumbnail = "켈리 사진";
    final SaveThemeRequest saveThemeRequest = new SaveThemeRequest(name, description, thumbnail);
    final Theme savedTheme = Theme.of(1L, name, description, thumbnail);

    given(themeRepository.save(saveThemeRequest.toTheme())).willReturn(savedTheme);

    // When
    final Theme theme = themeService.saveTheme(saveThemeRequest);

    // Then
    assertThat(theme.getId()).isEqualTo(1L);
  }

  @DisplayName("테마 정보를 삭제한다.")
  @Test
  void deleteThemeTest() {
    // Given
    final Long themeId = 1L;

    given(themeRepository.existById(themeId)).willReturn(true);
    willDoNothing().given(themeRepository).deleteById(themeId);

    // When & Then
    assertThatCode(() -> themeService.deleteTheme(themeId))
        .doesNotThrowAnyException();
  }
}
