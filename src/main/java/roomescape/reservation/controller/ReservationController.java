package roomescape.reservation.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.request.CreateReservationRequest;
import roomescape.reservation.dto.response.FindReservationResponse;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<FindReservationResponse>> getReservations() {
        return ResponseEntity.ok(reservationService.getReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindReservationResponse> getReservation(@PathVariable final Long id) {
        return ResponseEntity.ok(reservationService.getReservation(id));
    }

    @PostMapping
    public ResponseEntity<Void> createReservation(
            @RequestBody CreateReservationRequest createReservationRequest) {
        Long id = reservationService.createReservation(createReservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
