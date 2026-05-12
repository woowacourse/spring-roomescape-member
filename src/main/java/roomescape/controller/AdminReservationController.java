package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.ReservationRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.domain.Reservation;
import roomescape.service.ReservationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final ReservationService service;

    public AdminReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<ReservationResponse> reservations = service.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest request) {
        Reservation reservation = service.create(
                request.name(),
                request.date(),
                request.timeId(),
                request.themeId());
        return ResponseEntity.created(URI.create("/admin/reservations/" + reservation.getId()))
                .body(ReservationResponse.from(reservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
