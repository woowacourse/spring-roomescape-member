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
import roomescape.dto.request.AddThemeRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> addTheme(@Valid @RequestBody AddThemeRequest addThemeRequest) {
        Theme addedTheme = themeService.addTheme(addThemeRequest);
        ThemeResponse themeResponse = new ThemeResponse(addedTheme.getId(), addedTheme.getDescription(), addedTheme.getName(),
                addedTheme.getThumbnail());
        return ResponseEntity.created(URI.create("/themes/" + themeResponse.id())).body(themeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteThemeById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThemeResponse> getTheme(@PathVariable Long id) {
        Theme theme = themeService.getThemeById(id);
        ThemeResponse themeResponse = new ThemeResponse(theme.getId(), theme.getDescription(), theme.getName(),
                theme.getThumbnail());
        return ResponseEntity.ok(themeResponse);
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<Theme> themes = themeService.findAll();
        List<ThemeResponse> themeResponses = themes.stream()
                .map((theme) -> new ThemeResponse(theme.getId(), theme.getDescription(),
                        theme.getName(), theme.getThumbnail()))
                .toList();
        return ResponseEntity.ok(themeResponses);
    }
}
