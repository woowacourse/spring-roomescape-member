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
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ThemeResponse;
import roomescape.dto.ThemeSaveRequest;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> saveTheme(@RequestBody final ThemeSaveRequest themeSaveRequest) {
        final ThemeResponse themeResponse = themeService.saveTheme(themeSaveRequest);
        return ResponseEntity.created(URI.create("/thems/" + themeResponse.id()))
                .body(themeResponse);
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        final List<ThemeResponse> themeResponses = themeService.getThemes();
        return ResponseEntity.ok(themeResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable("id") final Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
