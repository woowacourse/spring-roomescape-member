package roomescape.controller;

import java.net.URI;
import java.time.LocalDate;
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
import roomescape.domain.Theme;
import roomescape.dto.CreateThemeRequest;
import roomescape.dto.PopularThemeResponse;
import roomescape.dto.ThemeReservationTimeResponse;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/admin/themes")
public class AdminThemeController {

    private final ThemeService themeService;

    public AdminThemeController(ThemeService themeService) {
        this.themeService = themeService;
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
    public ResponseEntity<List<ThemeReservationTimeResponse>> readThemeTimes(@PathVariable Long id,
                                                                             @RequestParam LocalDate date) {
        return ResponseEntity.ok(themeService.getThemeTimes(id, date));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<PopularThemeResponse>> readPopularThemes(@RequestParam Integer limit) {
        return ResponseEntity.ok(themeService.getPopularThemes(limit));
    }
}
