package roomescape.controller.api;

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
import roomescape.controller.api.dto.request.ThemeCreateRequest;
import roomescape.controller.api.dto.response.ThemeResponse;
import roomescape.controller.api.dto.response.ThemesResponse;
import roomescape.service.ThemeService;
import roomescape.service.dto.output.ThemeOutput;

@RestController
@RequestMapping("/admin/themes")
public class AdminThemeApiController {

    private final ThemeService themeService;

    public AdminThemeApiController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody final ThemeCreateRequest request) {
        final ThemeOutput output = themeService.createTheme(request.toInput());
        return ResponseEntity.created(URI.create("/times/" + output.id()))
                .body(ThemeResponse.from(output));
    }

    @GetMapping("/popular")
    public ResponseEntity<ThemesResponse> findPopularThemes(
            @RequestParam final String date,
            @RequestParam(required = false, defaultValue = "10") final int limit) {
        final List<ThemeOutput> outputs = themeService.findPopularThemes(date, limit);
        return ResponseEntity.ok().body(ThemesResponse.from(outputs));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable final long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
