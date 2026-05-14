package roomescape.theme.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.theme.controller.dto.*;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.TimeAvailability;
import roomescape.theme.service.PopularTheme;
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
    public ResponseEntity<ThemesResponse> findThemes() {
        List<Theme> themes = themeService.findThemes();
        ThemesResponse response = ThemesResponse.from(themes);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/available-times")
    public ResponseEntity<ThemeAvailableTimesResponse> findAvailableTimes(@PathVariable Long id, @RequestParam LocalDate date) {
        List<TimeAvailability> timeAvailabilities = themeService.findAvailableTimes(id, date);
        ThemeAvailableTimesResponse response = ThemeAvailableTimesResponse.from(timeAvailabilities);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/popular")
    public ResponseEntity<PopularThemesResponse> findPopularThemes(@RequestParam int days, @RequestParam int limit) {
        List<PopularTheme> popularThemes = themeService.findPopularThemes(LocalDate.now(), days, limit);

        PopularThemesResponse response = PopularThemesResponse.from(popularThemes);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
