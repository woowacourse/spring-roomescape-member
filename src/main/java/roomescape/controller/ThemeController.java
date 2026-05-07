package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.dto.PopularThemeProjection;
import roomescape.dto.response.PopularThemeResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/api/v1/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<Theme> themes = themeService.getThemes();
        List<ThemeResponse> themeResponses = ThemeResponse.fromAll(themes);
        return ResponseEntity.ok().body(themeResponses);
    }

    @GetMapping(params = {"from", "to"})
    public ResponseEntity<List<PopularThemeResponse>> getPopularTheme(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {
        List<PopularThemeProjection> popularThemes = themeService.getPopularThemes(from, to);
        List<PopularThemeResponse> popularThemeResponses = PopularThemeResponse.fromAll(popularThemes);
        return ResponseEntity.ok().body(popularThemeResponses);
    }
}
