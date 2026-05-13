package roomescape.reservation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
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
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationsResponse;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@RequestBody @Valid ReservationRequest reservationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.create(reservationRequest));
    }

    @GetMapping
    public ResponseEntity<ReservationsResponse> read(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Max(100) int size
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.read(page, size));
    }

    @GetMapping(params = {"user_name"})
    public ResponseEntity<ReservationsResponse> read(
            @RequestParam("user_name") String userName
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.readByUserName(userName));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        LocalDateTime now = LocalDateTime.now();
        reservationService.delete(id, now);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
