package roomescape.time;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.time.dto.ReservationTimeRequest;
import roomescape.time.dto.ReservationTimeResponse;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(
            @Valid @RequestBody ReservationTimeRequest reservationTimeRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationTimeService.create(reservationTimeRequest));
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> read() {
        return ResponseEntity.status(HttpStatus.OK).body(reservationTimeService.read());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/availablity")
    public ResponseEntity<List<ReservationTimeResponse>> readAvailableTimes(
            @RequestParam @NotNull Long themeId,
            @RequestParam @NotNull LocalDate date) {
        return ResponseEntity.status(HttpStatus.OK).body(reservationTimeService.readAvailableTimes(themeId, date));
    }
}
