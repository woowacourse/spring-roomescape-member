package roomescape.time;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.time.dto.ReservationTimeResponse;

@RestController
@RequestMapping("/api/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> read() {
        return ResponseEntity.status(HttpStatus.OK).body(reservationTimeService.read());
    }

    @GetMapping("/availablity")
    public ResponseEntity<List<ReservationTimeResponse>> readAvailableTimes(
            @RequestParam("theme_id") @NotNull Long themeId,
            @RequestParam @NotNull LocalDate date) {
        return ResponseEntity.status(HttpStatus.OK).body(reservationTimeService.readAvailableTimes(themeId, date));
    }
}
