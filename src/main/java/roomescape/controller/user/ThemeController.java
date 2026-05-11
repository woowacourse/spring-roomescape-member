package roomescape.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Period;
import roomescape.domain.Theme;
import roomescape.response.ReservationTimeResponse;
import roomescape.response.ThemeResponse;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;
    private final ReservationTimeService reservationTimeService;
    private final Clock clock;

    public ThemeController(ThemeService themeService, ReservationTimeService reservationTimeService, Clock clock) {
        this.themeService = themeService;
        this.reservationTimeService = reservationTimeService;
        this.clock = clock;
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
                ReservationTimeResponse.from(reservationTimeService.getAvailableTimes(themeId, resolveDate(date)));

        return ResponseEntity.ok(reservationTimeResponses);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> getPopularThemes() {
        List<Theme> themes = themeService.findPopularThemes(Period.lastWeek(clock));

        return ResponseEntity.ok(ThemeResponse.from(themes));
    }

    private LocalDate resolveDate(LocalDate date) {
        if (date == null) {
            return LocalDate.now(clock);
        }
        return date;
    }
}
