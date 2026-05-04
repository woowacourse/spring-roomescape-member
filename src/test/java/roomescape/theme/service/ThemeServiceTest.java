package roomescape.theme.service;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.theme.ThemeRepository;
import roomescape.theme.ThemeService;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeCreateRequest;
import roomescape.theme.dto.ThemeResponse;

@ExtendWith(MockitoExtension.class)
public class ThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ThemeService themeService;

    @DisplayName("테마의 정상 추가를 테스트합니다.")
    @Test
    void save_theme_successfully() {
        Theme themeBeforeSaved = Theme.create("theme name", "theme description", "theme img url");
        Theme themeAfterSaved = Theme.create(1L, themeBeforeSaved);
        Mockito.when(themeRepository.save(themeBeforeSaved)).thenReturn(themeAfterSaved);

        ThemeCreateRequest createRequestDto = new ThemeCreateRequest("theme name", "theme description", "theme img url");
        ThemeResponse themeResponse = themeService.saveTheme(createRequestDto);

        SoftAssertions.assertSoftly(assertSoftly -> {
            assertSoftly.assertThat(themeResponse.id()).isEqualTo(1L);
            assertSoftly.assertThat(themeResponse.name()).isEqualTo("theme name");
            assertSoftly.assertThat(themeResponse.description()).isEqualTo("theme description");
            assertSoftly.assertThat(themeResponse.thumbnailImgUrl()).isEqualTo("theme img url");
        });

        Mockito.verify(themeRepository).save(themeBeforeSaved);
    }

    @DisplayName("중복된 테마 추가 시 예외 발생을 테스트합니다.")
    @Test
    void save_duplicated_theme_exception() {
        Theme themeBeforeSaved = Theme.create("theme name", "theme description", "theme img url1");
        Mockito.when(themeRepository.existsByNameAndDescription(themeBeforeSaved)).thenReturn(true);

        ThemeCreateRequest createRequestDto = new ThemeCreateRequest("theme name", "theme description", "theme img url2");

        Assertions.assertThatThrownBy(() -> themeService.saveTheme(createRequestDto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테마의 삭제를 테스트합니다.")
    @Test
    void delete_theme() {
        themeService.delete(1L);

        Mockito.verify(themeRepository).delete(1L);
    }
}
