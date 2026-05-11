package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.AvailableReservationTimeResponse;
import roomescape.controller.dto.ThemeResponse;
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
        final List<ThemeResponse> responses = themeService.getAllThemes()
                .stream()
                .map(ThemeResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> getPopularThemes(
            @RequestParam(defaultValue = "10") int size) {
        final LocalDate today = LocalDate.now();
        final LocalDate startDate = today.minusDays(7);
        final LocalDate endDate = today.minusDays(1);
        final List<ThemeResponse> responses = themeService.getPopularThemes(size, startDate, endDate)
                .stream()
                .map(ThemeResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}/times")
    public ResponseEntity<List<AvailableReservationTimeResponse>> getReservationTimes(
            @PathVariable long id,
            @RequestParam LocalDate date) {
        final List<AvailableReservationTimeResponse> responses = themeService.getAvailableTimes(id, date)
                .stream()
                .map(AvailableReservationTimeResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
