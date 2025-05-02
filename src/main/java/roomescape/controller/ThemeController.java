package roomescape.controller;

import jakarta.validation.Valid;
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
import roomescape.dto.request.ThemeCreationRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public List<ThemeResponse> getThemes() {
        List<Theme> themes = themeService.findAllTheme();
        return themes.stream()
                .map(ThemeResponse::new)
                .toList();
    }

    @GetMapping("/top")
    public List<ThemeResponse> getTopThemes() {
        List<Theme> themes = themeService.findTopThemes();
        return themes.stream()
                .map(ThemeResponse::new)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(
            @Valid @RequestBody ThemeCreationRequest request) {
        long savedId = themeService.addTheme(request);
        Theme theme = themeService.findThemeById(savedId);
        return ResponseEntity
                .created(URI.create("theme/" + savedId))
                .body(new ThemeResponse(theme));
    }

    @DeleteMapping("/{themeId}")
    public ResponseEntity<Void> deleteById(
            @PathVariable("themeId") long id
    ) {
        themeService.deleteThemeById(id);
        return ResponseEntity.noContent().build();
    }
}
