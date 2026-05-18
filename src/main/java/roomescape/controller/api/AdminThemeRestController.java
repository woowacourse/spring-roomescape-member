package roomescape.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.theme.dto.ThemeCreateRequest;
import roomescape.domain.theme.dto.ThemeResponse;
import roomescape.service.ThemeService;

import java.net.URI;
import java.util.List;

@RequestMapping("/admin/themes")
@RestController
public class AdminThemeRestController {

    private final ThemeService themeService;

    public AdminThemeRestController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> create(@Valid @RequestBody ThemeCreateRequest themeRequest) {
        ThemeResponse newTheme = themeService.create(themeRequest);
        return ResponseEntity.created(URI.create("/admin/themes/" + newTheme.getId())).body(newTheme);
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> readAll() {
        return ResponseEntity.ok().body(themeService.findAll());
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> readPopularTheme(
            @RequestParam(defaultValue = "7") Integer period,
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        return ResponseEntity.ok().body(themeService.findPopularTheme(period, limit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

