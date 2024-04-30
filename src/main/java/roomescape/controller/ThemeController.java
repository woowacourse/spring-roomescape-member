package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.ThemeCreateRequest;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

import java.net.URI;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody ThemeCreateRequest themeCreateRequest) {
        ThemeResponse theme = themeService.createTheme(themeCreateRequest);
        return ResponseEntity.created(URI.create("/themes/" + theme.id()))
                .body(theme);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThemeResponse> readTheme(@PathVariable Long id) {
        ThemeResponse themeResponse = themeService.readTheme(id);
        return ResponseEntity.ok(themeResponse);
    }
}
