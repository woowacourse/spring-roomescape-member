package roomescape.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.dto.CreateReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> readReservations() {
        return ResponseEntity.ok().body(reservationService.getReservations().stream()
                .map(ReservationResponse::from)
                .toList());
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody CreateReservationRequest createReservationRequest) {
        Reservation createdReservation = reservationService.createReservation(
                createReservationRequest.name(),
                createReservationRequest.date(),
                createReservationRequest.timeId()
        );

        return ResponseEntity.ok().body(ReservationResponse.from(createdReservation));
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok().build();
    }

}
