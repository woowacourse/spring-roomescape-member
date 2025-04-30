package roomescape.unit.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.Theme;
import roomescape.dto.AddThemeDto;
import roomescape.repository.ThemeRepository;
import roomescape.service.ThemeService;
import roomescape.unit.repository.FakeThemeRepository;

class ThemeServiceTest {

    private ThemeService themeService;
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        themeRepository = new FakeThemeRepository();
        themeService = new ThemeService(themeRepository);
    }

    @Test
    void 테마를_추가할_수_있다() {
        AddThemeDto addThemeDto = new AddThemeDto("방탈출", "게임입니다.", "thumbnail");
        long id = themeService.addTheme(addThemeDto);

        assertThat(id).isEqualTo(1L);
    }

    @Test
    void 테마를_조회할_수_있다() {
        Theme theme = new Theme(null, "방탈출", "게임입니다.", "thumbnail");
        themeRepository.add(theme);

        assertThat(themeService.findAll()).hasSize(1);
    }
}
