package roomescape.domain.reservation.controller;

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
import roomescape.domain.reservation.request.ReservationCreateRequest;
import roomescape.domain.reservation.response.ReservationResponse;
import roomescape.domain.reservation.response.ReservationsResponse;
import roomescape.domain.reservation.service.ReservationService;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<ReservationsResponse> findAll() {
        ReservationsResponse reservations = reservationService.findAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> save(@RequestBody ReservationCreateRequest request) {
        ReservationResponse response = reservationService.saveReservation(request);
        return ResponseEntity.created(URI.create("/reservations/" + response.id()))
                .body(response);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long reservationId) {
        reservationService.deleteReservationBy(reservationId);
        return ResponseEntity.noContent().build();
    }
}
