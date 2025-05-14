package roomescape.theme.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.response.AvailableReservationResponse;
import roomescape.theme.dto.response.ThemeResponse;
import roomescape.theme.dto.response.ThemeSelectElementResponse;
import roomescape.theme.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> getTopTenTheme() {
        return ResponseEntity.ok(themeService.getTopTenTheme());
    }

    @GetMapping("/all")
    public ResponseEntity<List<ThemeSelectElementResponse>> getAllThemeSelectElements() {
        List<ThemeSelectElementResponse> themes = themeService.getAllThemes();
        return ResponseEntity.ok(themes);
    }

    @GetMapping("/{id}/times")
    public ResponseEntity<List<AvailableReservationResponse>> getThemesTimesWithStatus(
            @PathVariable(name = "id") Long themeId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        return ResponseEntity.ok(themeService.getThemesTimesWithStatus(themeId, date));
    }
}
