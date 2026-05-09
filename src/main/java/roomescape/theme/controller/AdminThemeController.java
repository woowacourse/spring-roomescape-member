package roomescape.theme.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.theme.controller.dto.ThemeRequest;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin/themes")
public class AdminThemeController {

    private final ThemeService themeService;

    public AdminThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> readAll() {
        List<ThemeResponse> responses = themeService.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> create(@Valid @RequestBody ThemeRequest requestDto) {
        Theme theme = themeService.save(requestDto);
        ThemeResponse response = ThemeResponse.from(theme);
        return ResponseEntity
                .created(URI.create("/themes/" + response.id()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ThemeResponse> update(@PathVariable Long id, @Valid @RequestBody ThemeRequest requestDto) {
        Theme theme = themeService.update(id, requestDto);
        return ResponseEntity.ok(ThemeResponse.from(theme));
    }
}
