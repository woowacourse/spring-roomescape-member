package roomescape.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.ThemeResponse;
import roomescape.dto.ThemeSaveRequest;
import roomescape.service.ThemeService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> saveTheme(@RequestBody @Valid final ThemeSaveRequest themeSaveRequest) {
        final ThemeResponse themeResponse = themeService.saveTheme(themeSaveRequest);
        return ResponseEntity.created(URI.create("/themes/" + themeResponse.id()))
                .body(themeResponse);
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        final List<ThemeResponse> themeResponses = themeService.getThemes();
        return ResponseEntity.ok(themeResponses);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> getPopularThemes(@RequestParam("date") final LocalDate localDate) {
        final List<ThemeResponse> themeResponses = themeService.getPopularThemes(localDate);
        return ResponseEntity.ok(themeResponses);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable("id") final Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
