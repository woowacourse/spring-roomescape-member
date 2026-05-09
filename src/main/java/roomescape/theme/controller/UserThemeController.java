package roomescape.theme.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;
import roomescape.time.controller.dto.ReservationTimeResponse;
import roomescape.time.service.ReservationTimeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class UserThemeController {

    private final ThemeService themeService;
    private final ReservationTimeService reservationTimeService;

    public UserThemeController(ThemeService themeService, ReservationTimeService reservationTimeService) {
        this.themeService = themeService;
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> readAll() {
        List<ThemeResponse> responses = themeService.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> readPopular() {
        List<ThemeResponse> responses = themeService.findPopularThemes()
                .stream()
                .map(ThemeResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{themeId}/available-times")
    public ResponseEntity<List<ReservationTimeResponse>> readAvailable(
            @PathVariable Long themeId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<ReservationTimeResponse> responses =
                reservationTimeService.findAvailableTimes(themeId, date)
                        .stream()
                        .map(ReservationTimeResponse::from)
                        .toList();
        return ResponseEntity.ok(responses);
    }
}
