package roomescape.theme.theme.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.theme.theme.dto.ThemeResponse;
import roomescape.theme.theme.dto.ThemeSaveRequest;
import roomescape.theme.theme.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/themes")
public class ThemeRestController {

    private final ThemeService themeService;

    public ThemeRestController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<ThemeResponse> responses = themeService.findAllThemes();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<ThemeResponse>> getBestThemes() {
        List<ThemeResponse> responses = themeService.findBestThemes();

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody ThemeSaveRequest request) {
        ThemeResponse response = themeService.save(request);

        URI location = URI.create("/themes/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteThemeById(id);

        return ResponseEntity.noContent().build();
    }
}
