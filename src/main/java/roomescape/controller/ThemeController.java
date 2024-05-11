package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.dto.ThemeRequest;
import roomescape.domain.dto.ThemeResponse;
import roomescape.domain.dto.ThemeResponses;
import roomescape.service.ThemeService;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<ThemeResponses> findAll() {
        return ResponseEntity.ok(themeService.findAll());
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> create(@RequestBody final ThemeRequest themeRequest) {
        ThemeResponse themeResponse = themeService.create(themeRequest);
        return ResponseEntity.created(URI.create("/themes/" + themeResponse.id())).body(themeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        themeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rank")
    public ResponseEntity<ThemeResponses> read(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate, @RequestParam Long size) {
        return ResponseEntity.ok(themeService.getPopularThemeList(startDate, endDate, size));
    }
}
