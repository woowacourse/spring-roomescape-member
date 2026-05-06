package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.response.ThemeResponse;
import roomescape.service.ThemeService;

import java.util.List;

@RestController("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<ThemeResponse> themeResponses = ThemeResponse.from(themeService.getThemes());

        return ResponseEntity.ok(themeResponses);
    }
}
