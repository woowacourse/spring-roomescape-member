package roomescape.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreationRequest;
import roomescape.repository.ThemeRepository;
import roomescape.test.fake.FakeThemeRepository;

class ThemeServiceTest {

    private final ThemeRepository themeRepository = new FakeThemeRepository();
    private final ThemeService themeService = new ThemeService(themeRepository);

    @DisplayName("테마를 추가할 수 있다.")
    @Test
    void canAddTheme() {
        ThemeCreationRequest request = new ThemeCreationRequest("theme", "description", "url");

        long savedId = themeService.addTheme(request);

        Theme savedTheme = themeRepository.findById(savedId).get();
        Theme expectedTheme = new Theme(1L, request.name(), request.description(), request.thumbnail());
        assertThat(savedTheme).isEqualTo(expectedTheme);
    }

    @DisplayName("ID를 통해 테마를 조회할 수 있다.")
    @Test
    void canFindThemeById() {
        long id = themeRepository.addTheme(Theme.createWithoutId("theme", "description", "url"));

        Theme actualTheme = themeService.findThemeById(id);

        Theme expectedTheme = new Theme(id, "theme", "description", "url");
        assertThat(actualTheme).isEqualTo(expectedTheme);
    }
}