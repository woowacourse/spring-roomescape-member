package roomescape.theme.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> findAll() {
        final List<Theme> themes = themeService.readAll();

        final List<ThemeResponse> themeResponses = changeToThemeResponses(themes);

        return ResponseEntity.ok(themeResponses);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> save(@RequestBody final ThemeRequest themeRequest) {
        final Theme theme = themeService.create(themeRequest.toTheme());

        final ThemeResponse themeResponse = changeToThemeResponse(theme);
        final String url = "/themes/" + themeResponse.id();

        return ResponseEntity.created(URI.create(url)).body(themeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        themeService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> findPopular() {
        List<Theme> themes = themeService.findPopular();

        List<ThemeResponse> themeResponses = changeToThemeResponses(themes);

        return ResponseEntity.ok(themeResponses);
    }

    private ThemeResponse changeToThemeResponse(final Theme theme) {
        return new ThemeResponse(theme);
    }

    private List<ThemeResponse> changeToThemeResponses(final List<Theme> themes) {
        return themes.stream()
                .map(this::changeToThemeResponse)
                .toList();
    }
}
