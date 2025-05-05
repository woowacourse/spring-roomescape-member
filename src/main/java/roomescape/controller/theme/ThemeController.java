package roomescape.controller.theme;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.controller.theme.dto.AddThemeRequest;
import roomescape.controller.theme.dto.PopularThemeRequest;
import roomescape.controller.theme.dto.ThemeResponse;
import roomescape.service.ThemeService;

@Controller
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService service;

    public ThemeController(final ThemeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> findAllThemes() {
        var themes = service.findAll();
        return ResponseEntity.ok(themes);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> addTheme(@RequestBody @Valid final AddThemeRequest request) {
        var theme = service.add(request);
        return ResponseEntity.created(URI.create("/themes/" + theme.id())).body(theme);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable("id") final Long id) {
        boolean isRemoved = service.removeById(id);
        if (isRemoved) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> findPopularThemes(@Valid final PopularThemeRequest request) {
        var themes = service.findPopularThemes(request.startDate(), request.endDate(), request.limit());
        return ResponseEntity.ok(themes);
    }
}
