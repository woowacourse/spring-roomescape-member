package roomescape.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
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
    public ResponseEntity<List<ThemeResponse>> search() {
        List<ThemeResponse> responses = themeService.getThemes().stream()
                .map(ThemeResponse::from)
                .toList();

        return ResponseEntity.ok().body(responses);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> add(@RequestBody ThemeRequest request) {
        Theme theme = new Theme(null, request.name(), request.description(), request.thumbnailImageUrl());
        ThemeResponse response = ThemeResponse.from(themeService.addTheme(theme));

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.deleteTheme(id);

        return ResponseEntity.ok().build();
    }
}
