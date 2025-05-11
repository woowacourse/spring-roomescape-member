package roomescape.controller.api.theme;

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
import roomescape.controller.api.theme.dto.AddThemeRequest;
import roomescape.controller.api.theme.dto.PopularThemeRequest;
import roomescape.controller.api.theme.dto.ThemeResponse;
import roomescape.service.ThemeService;

@Controller
@RequestMapping("/themes")
public class ThemeApiController {

    private final ThemeService service;

    public ThemeApiController(final ThemeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> findAllThemes() {
        final List<ThemeResponse> response = service.findAll();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> addTheme(@RequestBody @Valid final AddThemeRequest request) {
        final ThemeResponse response = service.add(request);
        return ResponseEntity.created(URI.create("/themes/" + response.id())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable("id") final Long id) {
        final boolean isRemoved = service.removeById(id);
        if (isRemoved) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> findPopularThemes(@Valid final PopularThemeRequest request) {
        final List<ThemeResponse> response = service.findPopularThemes(request.startDate(), request.endDate(),
                request.limit());
        return ResponseEntity.ok().body(response);
    }
}
