package roomescape.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.response.PopularThemeResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/themes")
@Validated
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<ThemeResponse> themeResponses = themeService.getThemes();
        return ResponseEntity.ok().body(themeResponses);
    }

    @GetMapping(path = "/popular", params = {"from", "to"})
    public ResponseEntity<List<PopularThemeResponse>> getPopularTheme(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {
        List<PopularThemeResponse> popularThemes = themeService.getPopularThemes(from, to);
        return ResponseEntity.ok().body(popularThemes);
    }
}
