package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreationRequest;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public List<Theme> getThemes() {
        return themeService.findAllTheme();
    }

    @GetMapping("/top")
    public List<Theme> getTopThemes() {
        return themeService.findTopThemes();
    }

    @PostMapping
    public ResponseEntity<Theme> createTheme(@RequestBody ThemeCreationRequest request) {
        long savedId = themeService.addTheme(request);
        Theme theme = themeService.findThemeById(savedId);
        return ResponseEntity.created(URI.create("theme/" + savedId)).body(theme);
    }

    @DeleteMapping("/{themeId}")
    public ResponseEntity<Theme> deleteById(@PathVariable("themeId") long id) {
        themeService.deleteThemeById(id);
        return ResponseEntity.noContent().build();
    }
}
