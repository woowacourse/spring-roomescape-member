package roomescape.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.theme.dto.RequestTheme;
import roomescape.theme.dto.ResponseTheme;
import roomescape.theme.service.ThemeService;
import roomescape.time.dto.RequestReservationTime;
import roomescape.time.dto.ResponseReservationTime;
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
    public ResponseTheme createTheme(@RequestBody RequestTheme requestTheme) {
        return ResponseTheme.from(themeService.createTheme(requestTheme.name(), requestTheme.description(), requestTheme.thumbnail()));
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> removeTheme(@PathVariable Long id) {
        themeService.removeTheme(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/times")
    public ResponseReservationTime createTime(@RequestBody RequestReservationTime request) {
        return ResponseReservationTime.from(reservationTimeService.createTime(request.startAt()));
    }

    @DeleteMapping("/times/{id}")
    public void removeTime(@PathVariable Long id) {
        reservationTimeService.removeTime(id);
    }
}
