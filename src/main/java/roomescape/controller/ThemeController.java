package roomescape.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/theme")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/{id}/available-time?date={date}")
    public ResponseEntity<List<ReservationTimeResponse>> getAvailableTime(
            @PathVariable long id,
            @PathVariable String date
    ) {
        List<ReservationTimeResponse> availableTimes = themeService.findAvailableTime(id, date);

        return ResponseEntity.ok().body(availableTimes);
    }
}
