package roomescape.controller;

import java.net.URI;
import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.dto.theme.CreateThemeRequest;
import roomescape.dto.theme.PopularThemeResponses;
import roomescape.dto.theme.ThemeReservationTimeResponses;
import roomescape.dto.theme.ThemeResponses;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/admin/themes")
public class AdminThemeController {

    private final ThemeService themeService;

    public AdminThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<ThemeResponses> readThemes() {
        return ResponseEntity.ok(ThemeResponses.from(themeService.getThemes()));
    }

    @PostMapping
    public ResponseEntity<Void> createTheme(@RequestBody CreateThemeRequest createThemeRequest) {
        Theme createdTheme = themeService.createTheme(createThemeRequest);
        URI location = URI.create("/admin/themes/" + createdTheme.getId());
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/times")
    public ResponseEntity<ThemeReservationTimeResponses> readThemeTimes(@PathVariable Long id,
                                                                        @RequestParam LocalDate date) {
        return ResponseEntity.ok(ThemeReservationTimeResponses.from(themeService.getThemeTimes(id, date)));
    }

    @GetMapping("/popular")
    public ResponseEntity<PopularThemeResponses> readPopularThemes(@RequestParam Integer limit) {
        return ResponseEntity.ok(PopularThemeResponses.from(themeService.getPopularThemes(limit)));
    }
}
