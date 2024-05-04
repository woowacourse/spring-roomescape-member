package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.theme.ThemeRequest;
import roomescape.dto.theme.ThemeResponse;
import roomescape.dto.theme.ThemesResponse;
import roomescape.service.ThemeService;

import java.net.URI;

@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public ResponseEntity<ThemesResponse> getAllThemes() {

        return ResponseEntity.ok(themeService.findAllThemes());
    }

    @GetMapping("/themes/top")
    public ResponseEntity<ThemesResponse> getTopNThemes(@RequestParam int count) {

        return ResponseEntity.ok(themeService.findTopNThemes(count));
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> saveTheme(@RequestBody ThemeRequest request) {
        ThemeResponse response = themeService.createTheme(request);

        return ResponseEntity.created(URI.create("/themes/" + response.id()))
                .body(response);
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> removeTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);

        return ResponseEntity.noContent().build();
    }
}
