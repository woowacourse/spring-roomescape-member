package roomescape.admin.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.dto.ThemeRequest;
import roomescape.admin.dto.ThemeResponse;
import roomescape.admin.service.ThemeService;
import roomescape.domain.Theme;

@RestController
@RequestMapping("/admin")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<Theme>> readAll() {
        List<Theme> themes = themeService.findAll();
        return ResponseEntity.ok(themes);
    }

    @GetMapping("/themes/{id}")
    public ResponseEntity<Theme> readById(@PathVariable Long id) {
        Theme theme = themeService.findById(id);
        return ResponseEntity.ok(theme);
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> create(@RequestBody ThemeRequest request) {
        Theme theme = themeService.add(
                request.name(),
                request.description(),
                request.image());
        ThemeResponse themeResponse = ThemeResponse.from(theme);
        return ResponseEntity.status(HttpStatus.CREATED).body(themeResponse);
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        themeService.removeById(id);
        return ResponseEntity.noContent().build();
    }
}
