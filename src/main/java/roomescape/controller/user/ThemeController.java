package roomescape.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.response.ReservationTimeResponse;
import roomescape.response.ThemeResponse;
import roomescape.service.ThemeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<ThemeResponse> themeResponses = ThemeResponse.from(themeService.getThemes());

        return ResponseEntity.ok(themeResponses);
    }

    @GetMapping("/{themeId}/times")
    public ResponseEntity<List<ReservationTimeResponse>> getAvailableTimes(
            @PathVariable Long themeId,
            @RequestParam(value = "date", required = false) LocalDate date) {

        List<ReservationTimeResponse> reservationTimeResponses =
                ReservationTimeResponse.from(themeService.getAvailableTimes(themeId, date));

        return ResponseEntity.ok(reservationTimeResponses);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> getPopularThemes() {
        List<Theme> themeResponses = themeService.findPopularThemes();

        return ResponseEntity.ok(ThemeResponse.from(themeResponses));
    }
}
