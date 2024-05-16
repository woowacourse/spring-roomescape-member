package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<Theme>> themes() {
        return ResponseEntity.ok(themeService.findAll());
    }

    @PostMapping
    public ResponseEntity<Theme> create(@RequestBody ThemeRequest themeRequest) {
        final Theme savedTheme = themeService.save(themeRequest);
        return ResponseEntity.created(URI.create("/times/" + savedTheme.getId())).body(savedTheme);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        themeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<Theme>> ranking() {
        List<Theme> themes = themeService.findTopRanking();
        return ResponseEntity.ok(themes);
    }
}
