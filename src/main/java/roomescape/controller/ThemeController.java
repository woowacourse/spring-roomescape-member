package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.service.dto.SaveThemeRequest;
import roomescape.service.dto.ThemeResponse;
import roomescape.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<Theme> themes = themeService.findThemes();
        return ResponseEntity.ok(ThemeResponse.listOf(themes));
    }

    @GetMapping("/themes/ranks")
    public ResponseEntity<List<ThemeResponse>> getThemeRanks() {
        List<Theme> themes = themeService.findTop10Recent7Days();
        return ResponseEntity.ok(ThemeResponse.listOf(themes));
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> addTheme(@RequestBody SaveThemeRequest request) {
        Theme theme = themeService.createTheme(request);
        return ResponseEntity.created(URI.create("/themes/" + theme.getId()))
                .body(ThemeResponse.of(theme));
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
