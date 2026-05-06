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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.dto.CreateThemeRequest;
import roomescape.dto.PopularThemeResponse;
import roomescape.dto.ThemeReservationTimeResponse;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/admin/themes")
    public ResponseEntity<Void> createReservation(
            @RequestBody CreateThemeRequest createThemeRequest) {
        Theme createdTheme = themeService.createTheme(createThemeRequest);

        URI location = URI.create("/admin/themes/" + createdTheme.getId());
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/admin/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/themes/{id}/times")
    public ResponseEntity<List<ThemeReservationTimeResponse>> readThemeTimes(@PathVariable Long id,
                                                                             @RequestParam LocalDate date) {
        List<ThemeReservationTimeResponse> themeTimes = themeService.getThemeTimes(id, date);
        return ResponseEntity.ok().body(themeTimes);
    }

    @GetMapping("/themes/popular")
    public ResponseEntity<List<PopularThemeResponse>> readPopularThemes(@RequestParam Integer limit) {
        List<PopularThemeResponse> popularThemes = themeService.getPopularThemes(limit);
        return ResponseEntity.ok().body(popularThemes);
    }
}
