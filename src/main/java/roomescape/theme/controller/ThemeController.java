package roomescape.theme.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.payload.ThemeResponse;
import roomescape.theme.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getAllThemes() {
        List<ThemeResponse> themeResponses = themeService.findAll().stream()
                .map(ThemeResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(themeResponses);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> getPopularThemes(
            @RequestParam("days") Integer days,
            @RequestParam("limit") Integer limit
    ) {
        List<ThemeResponse> popularThemeResponses = themeService.findPopularThemes(days, limit).stream()
                .map(ThemeResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(popularThemeResponses);
    }

}
