package roomescape.user.reservation;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.user.reservation.dto.ReservationRequest;
import roomescape.user.reservation.dto.ReservationResponse;
import roomescape.user.reservationtime.dto.TimeResponse;


@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(
        @RequestBody @Valid ReservationRequest request
    ) {
        ReservationResponse response = reservationService.createReservation(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<TimeResponse>> getReservations(
        @RequestParam LocalDate date, @RequestParam Long themeId
    ) {
        List<TimeResponse> responses = reservationService.getReservations(date, themeId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(
        @PathVariable Long id
    ) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

}
