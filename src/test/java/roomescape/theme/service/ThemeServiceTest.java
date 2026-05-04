package roomescape.theme.service;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.theme.repository.ThemeRepository;
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
        Theme themeBeforeSaved = Theme.builder()
                .name("theme name")
                .description("theme description")
                .thumbnailImgUrl("theme img url")
                .build();
        Theme themeAfterSaved = themeBeforeSaved.withId(1L);
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
        Theme themeBeforeSaved = Theme.builder()
                .name("theme name")
                .description("theme description")
                .thumbnailImgUrl("theme img url1")
                .build();
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

    @DisplayName("테마의 전체 조회 시 DTO 정상 변환을 테스트합니다.")
    @Test
    void find_all_themes_dto_transfer() {
        Theme theme1 = Theme.builder()
                .name("theme name1")
                .description("theme description1")
                .thumbnailImgUrl("theme img url1")
                .build();
        Mockito.when(themeRepository.findAll()).thenReturn(List.of(theme1));

        List<ThemeResponse> themeResponses = themeService.findAllThemes();

        SoftAssertions.assertSoftly(assertSoftly -> {
            assertSoftly.assertThat(themeResponses.getFirst().name()).isEqualTo("theme name1");
            assertSoftly.assertThat(themeResponses.getFirst().description()).isEqualTo("theme description1");
            assertSoftly.assertThat(themeResponses.getFirst().thumbnailImgUrl()).isEqualTo("theme img url1");
        });
    }

    @DisplayName("테마의 전체 조회를 테스트합니다.")
    @Test
    void find_all_themes() {
        Theme theme1 = Theme.builder()
                .name("theme name1")
                .description("theme description1")
                .thumbnailImgUrl("theme img url1")
                .build();
        Theme theme2 = Theme.builder()
                .name("theme name2")
                .description("theme description2")
                .thumbnailImgUrl("theme img url2")
                .build();
        Theme theme3 = Theme.builder()
                .name("theme name3")
                .description("theme description3")
                .thumbnailImgUrl("theme img url3")
                .build();
        Mockito.when(themeRepository.findAll()).thenReturn(List.of(theme1, theme2, theme3));

        List<ThemeResponse> themeResponses = themeService.findAllThemes();

        Assertions.assertThat(themeResponses.size()).isEqualTo(3);
    }
}
