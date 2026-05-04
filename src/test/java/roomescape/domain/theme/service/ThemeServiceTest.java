package roomescape.domain.theme.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.theme.dto.response.ThemeResponseDTO;
import roomescape.domain.theme.repository.FakeThemeRepository;
import roomescape.domain.theme.repository.ThemeRepository;

class ThemeServiceTest {

    private final ThemeService themeService;
    private final ThemeRepository themeRepository;

    ThemeServiceTest() {
        this.themeRepository = new FakeThemeRepository();
        this.themeService = new ThemeService(themeRepository);
    }

    @Nested
    class GetThemesTest {

        @Test
        void 성공() {
            // given

            // when
            List<ThemeResponseDTO> actual = themeService.getThemes();

            // then
            assertEquals(0, actual.size());
        }
    }
}