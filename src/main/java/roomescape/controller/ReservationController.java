package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.reservation.ReservationRequest;
import roomescape.domain.reservation.ReservationResponse;
import roomescape.service.ReservationService;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> readAll() {
        return reservationService.readAll();
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<ReservationResponse> read(@PathVariable Long id) {
        ReservationResponse reservationResponse = reservationService.read(id);
        return ResponseEntity.ok(reservationResponse);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(@RequestBody ReservationRequest reservationReq) {
        ReservationResponse newReservation = reservationService.create(reservationReq);
        URI uri = URI.create("/reservations/" + newReservation.getId());
        return ResponseEntity.created(uri).body(newReservation);
    }

    @PutMapping("/reservations/{id}")
    public ResponseEntity<Void> update(@RequestBody ReservationRequest newReservationReq, @PathVariable Long id) {
        reservationService.update(newReservationReq, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
