package roomescape.theme.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.dto.PopularThemesResponse;
import roomescape.theme.dto.ThemesResponse;
import roomescape.theme.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<ThemesResponse> findAll() {
        ThemesResponse responses = themeService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping(params = "sort")
    public ResponseEntity<PopularThemesResponse> findPopularThemes(
            @RequestParam String sort,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "7") int days) {
        PopularThemesResponse responses = themeService.findPopularThemes(sort, limit, days);
        return ResponseEntity.ok(responses);
    }
}
