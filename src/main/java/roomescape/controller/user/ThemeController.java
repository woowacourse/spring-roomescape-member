package roomescape.controller.user;

import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public List<ThemeResponse> getThemes() {
        List<Theme> themes = themeService.findAllTheme();
        return themes.stream()
                .map(ThemeResponse::new)
                .toList();
    }

    @GetMapping("/themes/top")
    public List<ThemeResponse> getTopThemes() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);
        List<Theme> themes = themeService.findTopThemes(startDate, endDate, 10);
        return themes.stream()
                .map(ThemeResponse::new)
                .toList();
    }
}
