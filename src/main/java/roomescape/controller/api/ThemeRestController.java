package roomescape.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.theme.ThemeRequest;
import roomescape.domain.theme.ThemeResponse;
import roomescape.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
public class ThemeRestController {

    private final ThemeService themeService;

    public ThemeRestController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/admin/themes")
    public ResponseEntity<ThemeResponse> create(@RequestBody ThemeRequest themeRequest) {
        ThemeResponse newTheme = themeService.create(themeRequest);
        return ResponseEntity.created(URI.create("/admin/themes/" + newTheme.getId())).body(newTheme);
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> readAll() {
        return ResponseEntity.ok().body(themeService.findAll());
    }

    @GetMapping("/themes/popular")
    public ResponseEntity<List<ThemeResponse>> readPopularTheme(
            @RequestParam(defaultValue = "7") Integer period,
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        return ResponseEntity.ok().body(themeService.findPopularTheme(period, limit));
    }

    @DeleteMapping("/admin/themes/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

