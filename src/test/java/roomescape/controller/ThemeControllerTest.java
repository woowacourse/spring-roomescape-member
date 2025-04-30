package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreationRequest;
import roomescape.repository.ThemeRepository;
import roomescape.service.ThemeService;
import roomescape.test.fake.FakeThemeRepository;

class ThemeControllerTest {

    private final ThemeRepository themeRepository = new FakeThemeRepository();
    private final ThemeService themeService = new ThemeService(themeRepository);
    private final ThemeController themeController = new ThemeController(themeService);

    @DisplayName("테마를 추가합니다.")
    @Test
    void createTheme() {
        ThemeCreationRequest request = new ThemeCreationRequest("themeName", "테마입니다.", "url");

        ResponseEntity<Theme> response = themeController.createTheme(request);

        Theme savedTheme = themeRepository.findAll().getFirst();
        Theme expectedTheme = new Theme(1L, request.name(), request.description(), request.thumbnail());
        assertAll(
                () -> assertThat(savedTheme).isEqualTo(expectedTheme),
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("theme/1"),
                () -> assertThat(response.getBody()).isEqualTo(expectedTheme)
        );
    }
}
