package roomescape.web.controller;

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
import roomescape.core.dto.theme.ThemeRequest;
import roomescape.core.dto.theme.ThemeResponse;
import roomescape.core.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> create(@RequestBody final ThemeRequest request) {
        validateRequest(request);
        final ThemeResponse response = themeService.create(request);
        return ResponseEntity.created(URI.create("/themes/" + response.getId()))
                .body(response);
    }

    private void validateRequest(final ThemeRequest request) {
        final String name = request.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        final String description = request.getDescription();
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }

        final String thumbnail = request.getThumbnail();
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new IllegalArgumentException("Thumbnail cannot be null or empty");
        }
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> findAll() {
        return ResponseEntity.ok(themeService.findAll());
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> findPopular() {
        return ResponseEntity.ok(themeService.findPopularTheme());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        themeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
