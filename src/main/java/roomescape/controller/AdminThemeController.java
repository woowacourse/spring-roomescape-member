package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.ThemeRequest;
import roomescape.controller.dto.ThemeResponse;
import roomescape.domain.Theme;
import roomescape.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin/themes")
public class AdminThemeController {

    private final ThemeService themeService;

    public AdminThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<ThemeResponse> themes = themeService.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
        return ResponseEntity.ok(themes);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody ThemeRequest request) {
        Theme theme = themeService.create(
                request.name(),
                request.description(),
                request.thumbnail());
        return ResponseEntity.created(URI.create("/admin/themes/" + theme.getId()))
                .body(ThemeResponse.from(theme));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
