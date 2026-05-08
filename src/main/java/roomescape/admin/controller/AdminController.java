package roomescape.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.ReservationTimeRequest;
import roomescape.time.dto.ReservationTimeResponse;
import roomescape.time.service.ReservationTimeService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ThemeService themeService;
    private final ReservationTimeService reservationTimeService;

    public AdminController(ThemeService themeService, ReservationTimeService reservationTimeService) {
        this.themeService = themeService;
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody ThemeRequest requestTheme) {
        Theme theme = themeService.createTheme(requestTheme.name(), requestTheme.description(), requestTheme.thumbnail());
        ThemeResponse response = ThemeResponse.from(theme);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> removeTheme(@PathVariable Long id) {
        themeService.removeTheme(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> createTime(@RequestBody ReservationTimeRequest request) {
        ReservationTime time = reservationTimeService.createTime(request.startAt());
        ReservationTimeResponse response = ReservationTimeResponse.from(time);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> removeTime(@PathVariable Long id) {
        reservationTimeService.removeTime(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
