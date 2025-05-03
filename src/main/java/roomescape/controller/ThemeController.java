package roomescape.controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeService;

import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getAll() {
        return ResponseEntity.ok().body(themeService.getAll());
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> create(@NotNull @RequestBody ThemeRequest request) {
        ThemeResponse themeResponse = themeService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(themeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> getTop10(@RequestParam(value = "top", defaultValue = "10") int count) {
        List<ThemeResponse> themes = themeService.getPopularThemes(count);
        return ResponseEntity.ok().body(themes);
    }
}
