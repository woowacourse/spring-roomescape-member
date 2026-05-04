package roomescape.theme;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ResponseTheme;
import roomescape.theme.service.ThemeService;

import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public List<ResponseTheme> getThemes() {
        List<Theme> themes = themeService.getThemes();
        return themes.stream()
                .map(ResponseTheme::from)
                .toList();
    }
}
