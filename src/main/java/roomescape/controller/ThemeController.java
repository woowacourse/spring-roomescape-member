package roomescape.controller;

import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.AvailableTimeResponse;
import roomescape.controller.dto.ThemeRequest;
import roomescape.controller.dto.ThemeResponse;
import roomescape.domain.AvailableTime;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;
    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;

    public ThemeController(ThemeService themeService, ReservationService reservationService,
                           ReservationTimeService reservationTimeService) {
        this.themeService = themeService;
        this.reservationService = reservationService;
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public List<ThemeResponse> getThemes() {
        return themeService.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody ThemeRequest request) {
        Theme theme = themeService.create(
                request.name(),
                request.description(),
                request.thumbnail());
        return ResponseEntity.created(URI.create("/themes/" + theme.getId()))
                .body(ThemeResponse.from(theme));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/times")
    public List<AvailableTime> getAvailableTimes(@PathVariable Long id,
                                                                 @RequestParam("date") LocalDate date) {
        return reservationService.findAvailableTime(id, date);
    }
}
