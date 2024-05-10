package roomescape.controller.rest;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.theme.ThemeRequest;
import roomescape.dto.theme.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> readThemes() {
        return ResponseEntity.ok(themeService.findAllThemes());
    }

    @GetMapping("/top")
    public ResponseEntity<List<ThemeResponse>> readTopNThemes(@RequestParam int count) {
        return ResponseEntity.ok(themeService.findTopNThemes(count));
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody @Valid ThemeRequest request) {
        ThemeResponse response = themeService.createTheme(request);

        return ResponseEntity.created(URI.create("/themes/" + response.id()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);

        return ResponseEntity.noContent().build();
    }
}
