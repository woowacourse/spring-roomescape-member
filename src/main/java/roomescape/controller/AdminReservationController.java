package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationRequest;
import roomescape.service.ReservationService;

import java.net.URI;

@RestController
public class AdminReservationController {
    private final ReservationService reservationService;

    public AdminReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<Reservation> create(@RequestBody ReservationRequest reservationRequest) {
        final Reservation reservation = reservationService.save(reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }
}
