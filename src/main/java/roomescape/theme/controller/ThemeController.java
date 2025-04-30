package roomescape.theme.controller;

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
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.dto.request.ThemeRequest;
import roomescape.theme.dto.response.ThemeResponse;
import roomescape.theme.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@Valid @RequestBody ThemeRequest request) {
        ThemeResponse themeResponse = themeService.create(request);
        return ResponseEntity.created(URI.create("/theme/" + themeResponse.id())).body(themeResponse);
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getAllTheme() {
        return ResponseEntity.ok(themeService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable("id") Long id) {
        themeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
