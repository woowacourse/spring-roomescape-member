package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.PopularThemeResponse;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
public class ThemeController {

    private final ThemeService service;

    public ThemeController(ThemeService service) {
        this.service = service;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> readAllTheme() {
        return ResponseEntity.ok(service.readAllTheme());
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> postTheme(@RequestBody ThemeRequest request) {
        ThemeResponse themeResponse = service.postTheme(request);
        URI location = URI.create("/themes/" + themeResponse.id());
        return ResponseEntity.created(location).body(themeResponse);
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable long id) {
        service.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/themes/popular")
    public ResponseEntity<List<PopularThemeResponse>> readRecentPopularThemes() {
        return ResponseEntity.ok(service.readRecentPopularThemes());
    }
}
