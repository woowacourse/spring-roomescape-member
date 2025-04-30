package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.ThemeCreateRequest;
import roomescape.dto.ThemeResponse;
import roomescape.entity.Theme;
import roomescape.service.ThemeService;

import java.net.URI;
import java.util.List;

@Controller
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> add(@RequestBody ThemeCreateRequest request) {
        Theme theme = themeService.add(request.name(), request.description(), request.thumbnail());
        return ResponseEntity.created(URI.create("/thems")).body(ThemeResponse.from(theme));
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<Theme> themes = themeService.getThemes();
        List<ThemeResponse> themeResponses = themes.stream().
                map(ThemeResponse::from)
                .toList();
        return ResponseEntity.ok(themeResponses);
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        themeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/themes/popular")
    public ResponseEntity<List<ThemeResponse>> getPopularThemes() {
        List<Theme> themes = themeService.getPopularThemes();
        List<ThemeResponse> themeResponses = themes.stream()
                .map(ThemeResponse::from)
                .toList();
        return ResponseEntity.ok(themeResponses);
    }
}
