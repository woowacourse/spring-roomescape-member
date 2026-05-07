package roomescape.theme.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ResponsePopularTheme;
import roomescape.theme.dto.ResponseTheme;
import roomescape.theme.dto.ResponseThemeAvailableTime;
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
    public ResponseEntity<List<ResponseTheme>> getThemes() {
        List<Theme> themes = themeService.getThemes();
        List<ResponseTheme> response = themes.stream()
                .map(ResponseTheme::from)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/available-times")
    public ResponseEntity<List<ResponseThemeAvailableTime>> getAvailableTimes(@PathVariable Long id, @RequestParam LocalDate date) {
        List<AvailableTime> availableTimes = themeService.getAvailableTimes(id, date);
        List<ResponseThemeAvailableTime> response = availableTimes.stream()
                .map(ResponseThemeAvailableTime::from)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ResponsePopularTheme>> getPopularThemes(@RequestParam int days, @RequestParam int limit) {
        List<PopularTheme> popularThemes = themeService.getPopularThemes(days, limit);
        List<ResponsePopularTheme> response = popularThemes.stream()
                .map(ResponsePopularTheme::from)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
