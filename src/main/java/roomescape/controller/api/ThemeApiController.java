package roomescape.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.request.ThemeCreateRequest;
import roomescape.controller.dto.response.ThemeResponse;
import roomescape.model.entity.Theme;
import roomescape.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
public class ThemeApiController {

    private final ThemeService themeService;

    public ThemeApiController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> add(@RequestBody @Valid ThemeCreateRequest request) {
        Theme theme = themeService.add(request.name(), request.description(), request.thumbnail());
        return ResponseEntity.created(URI.create("/themes")).body(ThemeResponse.from(theme));
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<Theme> themes = themeService.getAllThemes();
        List<ThemeResponse> responses = ThemeResponse.from(themes);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/themes/popular")
    public ResponseEntity<List<ThemeResponse>> getPopularThemes(@RequestParam(value = "size", defaultValue = "10") int size) {
        List<Theme> popularThemes = themeService.getPopularThemes(size);
        List<ThemeResponse> responses = ThemeResponse.from(popularThemes);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        themeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
