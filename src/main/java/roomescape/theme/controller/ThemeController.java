package roomescape.theme.controller;

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

import roomescape.theme.response.RankTheme;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<Theme>> findAll() {
        return ResponseEntity.ok(themeService.findAll());
    }

    @PostMapping
    public ResponseEntity<Theme> create(@RequestBody Theme theme) {
        Theme createdTheme = themeService.create(theme);
        return ResponseEntity.created(URI.create("/themes/" + createdTheme.id())).body(createdTheme);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rank")
    public ResponseEntity<List<RankTheme>> getRank() {
        return ResponseEntity.ok(themeService.getRank());
    }
}
