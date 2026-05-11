package roomescape.theme.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.theme.controller.dto.PopularThemeResponse;
import roomescape.theme.controller.dto.ThemeAvailableTimeResponse;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.AvailableTime;
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
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<Theme> themes = themeService.findThemes();
        List<ThemeResponse> response = themes.stream()
                .map(ThemeResponse::from)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/available-times")
    public ResponseEntity<List<ThemeAvailableTimeResponse>> getAvailableTimes(@PathVariable Long id, @RequestParam LocalDate date) {
        List<AvailableTime> availableTimes = themeService.findAvailableTimes(id, date);
        List<ThemeAvailableTimeResponse> response = availableTimes.stream()
                .map(ThemeAvailableTimeResponse::from)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<PopularThemeResponse>> getPopularThemes(@RequestParam int days, @RequestParam int limit) {
        List<PopularTheme> popularThemes = themeService.findPopularThemes(days, limit);
        List<PopularThemeResponse> response = popularThemes.stream()
                .map(PopularThemeResponse::from)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
