package roomescape.time;

import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.time.dto.ReservationTimesResponse;

@RestController
@RequestMapping("/api/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<ReservationTimesResponse> read() {
        return ResponseEntity.status(HttpStatus.OK).body(reservationTimeService.read());
    }

    @GetMapping("/available")
    public ResponseEntity<ReservationTimesResponse> readAvailableTimes(
            @RequestParam("theme_id") Long themeId,
            @RequestParam LocalDate date) {
        return ResponseEntity.status(HttpStatus.OK).body(reservationTimeService.readAvailableTimes(themeId, date));
    }
}
