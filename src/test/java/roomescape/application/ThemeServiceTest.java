package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Theme;
import roomescape.domain.ThemeName;
import roomescape.domain.ThemeRepository;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @InjectMocks
    private ThemeService themeService;

    @Mock
    private ThemeRepository themeRepository;

    @DisplayName("테마를 생성한다.")
    @Test
    void shouldReturnCreatedTheme() {
        ThemeRequest request = new ThemeRequest("테마", "테마 설명", "url");
        given(themeRepository.create(any(Theme.class)))
                .willReturn(new Theme(1L, new ThemeName("테마"), "테마 설명", "url"));

        ThemeResponse themeResponse = themeService.create(request);

        then(themeRepository).should().create(request.toTheme());
    }

    @DisplayName("모든 테마 조회시 themeRepository findAll 메서드를 1회 호출하고 모든 테마를 반환한다.")
    @Test
    void shouldReturnAllThemes() {
        List<ThemeResponse> responses = themeService.findAll();
        assertThat(responses).isEmpty();
        then(themeRepository).should().findAll();
    }

    @DisplayName("테마 삭제시 themeRepository deleteById 메서드를 1회 호출한다.")
    @Test
    void shouldDeleteThemeById() {
        themeService.deleteById(1L);
        then(themeRepository).should().deleteById(1L);
    }
}
