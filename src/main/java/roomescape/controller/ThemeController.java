package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.Theme;
import roomescape.dto.SaveThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public List<ThemeResponse> getThemes() {
        return themeService.getThemes()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> saveTheme(@RequestBody final SaveThemeRequest request) {
        final Theme savedTheme = themeService.saveTheme(request);

        return ResponseEntity.created(URI.create("/themes/" + savedTheme.getId()))
                .body(ThemeResponse.from(savedTheme));
    }

    @DeleteMapping("/{themeId}")
    public ResponseEntity<Void> deleteTheme(@PathVariable final Long themeId) {
        themeService.deleteTheme(themeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular-themes")
    public List<ThemeResponse> getPopularThemes() {
        return themeService.getPopularThemes()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
