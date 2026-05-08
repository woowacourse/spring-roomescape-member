package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

@RequestMapping("/themes")
@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getAllThemes() {
        return ResponseEntity.ok(themeService.getAllThemes());
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> getPopularThemes(
            @RequestParam(defaultValue = "10") int size) {
        final LocalDate today = LocalDate.now();
        final LocalDate startDate = today.minusDays(7);
        final LocalDate endDate = today.minusDays(1);
        return ResponseEntity.ok(themeService.getPopularThemes(size, startDate, endDate));
    }

    @GetMapping("/{id}/times")
    public ResponseEntity<List<AvailableReservationTimeResponse>> getReservationTimes(
            @PathVariable long id,
            @RequestParam LocalDate date) {
        final List<AvailableReservationTimeResponse> reservationTimeResponses = themeService.getAvailableTimeResponses(
                id, date);
        return ResponseEntity.ok(reservationTimeResponses);
    }
}
