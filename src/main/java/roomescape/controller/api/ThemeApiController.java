package roomescape.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.theme.ThemeRequest;
import roomescape.controller.dto.theme.ThemeResponse;
import roomescape.entity.Theme;
import roomescape.service.ThemeService;

import java.util.List;

@RestController
public class ThemeApiController {

    private final ThemeService themeService;

    public ThemeApiController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> add(@RequestBody @Valid ThemeRequest request) {
        Theme theme = themeService.add(request.name(), request.description(), request.thumbnail());
        return ResponseEntity.status(HttpStatus.CREATED).body(ThemeResponse.from(theme));
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<Theme> themes = themeService.getAllThemes();
        List<ThemeResponse> responses = ThemeResponse.from(themes);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/themes/popular")
    public ResponseEntity<List<ThemeResponse>> getPopularThemes() {
        List<Theme> popularThemes = themeService.getPopularThemes();
        List<ThemeResponse> responses = ThemeResponse.from(popularThemes);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        themeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
