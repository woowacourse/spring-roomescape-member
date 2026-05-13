package roomescape.reservationtime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/times")
public class UserReservationTimeController {
    private final UserReservationTimeService userReservationTimeService;

    public UserReservationTimeController(UserReservationTimeService userReservationTimeService) {
        this.userReservationTimeService = userReservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes() {
        List<ReservationTime> reservationTimes = userReservationTimeService.findReservationTimes();
        List<ReservationTimeResponse> response = reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{themeId}")
    public ResponseEntity<ScheduleResponse> getSchedules(@PathVariable long themeId,
                                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        ScheduleResponse response = userReservationTimeService.getSchedules(themeId, date);
        return ResponseEntity.ok().body(response);
    }
}
