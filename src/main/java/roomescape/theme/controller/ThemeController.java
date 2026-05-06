package roomescape.theme.controller;

import org.springframework.web.bind.annotation.*;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ResponseTheme;
import roomescape.theme.dto.ResponseThemeAvailableTime;
import roomescape.theme.service.AvailableTime;
import roomescape.theme.service.ThemeService;

import java.time.LocalDate;
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

    @GetMapping("/{id}/available-times")
    public List<ResponseThemeAvailableTime> getAvailableTimes(@PathVariable Long id, @RequestParam LocalDate date) {
        List<AvailableTime> availableTimes = themeService.getAvailableTimes(id, date);
        return availableTimes.stream()
                .map(ResponseThemeAvailableTime::from)
                .toList();
    }
}
