package roomescape.presentation;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.ReservationQueryService;
import roomescape.application.dto.response.AvailableTimeResponse;
import roomescape.application.dto.response.ThemeResponse;

@RestController
public class ReservationQueryController {
    private final ReservationQueryService reservationQueryService;

    public ReservationQueryController(ReservationQueryService reservationQueryService) {
        this.reservationQueryService = reservationQueryService;
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<AvailableTimeResponse>> findAvailableTimes(@RequestParam LocalDate date,
                                                                          @RequestParam long themeId) {
        List<AvailableTimeResponse> responses = reservationQueryService.findAvailableTimes(date, themeId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/popular-themes")
    public ResponseEntity<List<ThemeResponse>> findPopularThemes() {
        List<ThemeResponse> responses = reservationQueryService.findPopularThemes();
        return ResponseEntity.ok(responses);
    }
}
