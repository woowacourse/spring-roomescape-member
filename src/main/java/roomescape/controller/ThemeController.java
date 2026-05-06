package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.dto.PopularThemeResponse;
import roomescape.dto.ThemeReservationTimeResponse;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> readThemes() {
        List<Theme> themes = themeService.getThemes();
        return ResponseEntity.ok().body(themes.stream()
                .map(ThemeResponse::from)
                .toList());
    }

    @GetMapping("/{id}/times")
    public ResponseEntity<List<ThemeReservationTimeResponse>> readThemeTimes(@PathVariable Long id,
                                                                             @RequestParam LocalDate date) {
        List<ThemeReservationTimeResponse> themeTimes = themeService.getThemeTimes(id, date);
        return ResponseEntity.ok().body(themeTimes);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<PopularThemeResponse>> readPopularThemes(@RequestParam Integer limit) {
        List<PopularThemeResponse> popularThemes = themeService.getPopularThemes(limit);
        return ResponseEntity.ok().body(popularThemes);
    }
}
