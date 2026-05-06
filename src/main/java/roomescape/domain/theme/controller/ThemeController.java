package roomescape.domain.theme.controller;

import java.net.URI;
import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.theme.request.ThemeCreateRequest;
import roomescape.domain.theme.response.ThemeReservationTimesResponse;
import roomescape.domain.theme.response.ThemeResponse;
import roomescape.domain.theme.response.ThemesResponse;
import roomescape.domain.theme.service.ThemeService;

@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public ResponseEntity<ThemesResponse> getAllThemes() {
        ThemesResponse themes = themeService.findAllThemes();
        return ResponseEntity.ok(themes);
    }

    @GetMapping("/themes/{themeId}/times")
    public ResponseEntity<ThemeReservationTimesResponse> getAllThemeReservationTimes(
            @PathVariable Long themeId,
            @RequestParam LocalDate date
    ) {
        ThemeReservationTimesResponse times = themeService.findAllThemeReservationTimes(themeId, date);
        return ResponseEntity.ok(times);
    }

    @PostMapping("/admin/themes")
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody ThemeCreateRequest request) {
        ThemeResponse theme = themeService.saveTheme(request);
        return ResponseEntity.created(URI.create("/themes/" + theme.id()))
                .body(theme);
    }

    @DeleteMapping("/admin/themes/{themeId}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long themeId) {
        themeService.deleteThemeById(themeId);
        return ResponseEntity.noContent().build();
    }
}
