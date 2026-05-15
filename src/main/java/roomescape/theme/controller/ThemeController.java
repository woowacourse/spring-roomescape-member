package roomescape.theme.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.theme.dto.PopularThemeRequest;
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
            @Valid @ModelAttribute PopularThemeRequest request) {
        PopularThemesResponse responses = themeService.findPopularThemes(request.sort(), request.limit(), request.days());
        return ResponseEntity.ok(responses);
    }
}
