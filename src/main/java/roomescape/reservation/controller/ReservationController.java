package roomescape.reservation.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.ReservationRequest;
import roomescape.reservation.service.dto.ReservationResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationResponse> findAllReservations() {
        return reservationService.findAll();
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody @Valid ReservationRequest reservationRequest) {
        ReservationResponse reservationResponse = reservationService.create(reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
