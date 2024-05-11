package roomescape.reservation.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.AdminReservationRequest;
import roomescape.reservation.service.dto.ReservationResponse;

import java.net.URI;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {
    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody @Valid AdminReservationRequest adminReservationRequest) {
        ReservationResponse reservationResponse = reservationService.create(adminReservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
