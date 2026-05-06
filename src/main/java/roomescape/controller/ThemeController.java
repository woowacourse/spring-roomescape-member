package roomescape.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreateRequest;
import roomescape.dto.ThemeDeleteRequest;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/api/v1/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<Theme>> getThemes() {
        List<Theme> themes = themeService.getThemes();
        return ResponseEntity.ok().body(themes);
    }

    @PostMapping
    public ResponseEntity<Theme> createTheme(@RequestBody ThemeCreateRequest request) {
        Theme theme = themeService.createTheme(
                request.name(),
                request.description(),
                request.imgUrl(),
                request.userName()
        );
        return ResponseEntity.created(URI.create("/api/v1/themes/" + theme.getId())).body(theme);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id,
                                            @RequestBody ThemeDeleteRequest request) {
        themeService.deleteTheme(id, request.userName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping(params = {"from", "to"})
    public ResponseEntity<List<Theme>> getPopularTheme(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {
        List<Theme> themes = themeService.getPopularThemes(from, to);
        return ResponseEntity.ok().body(themes);
    }
}
