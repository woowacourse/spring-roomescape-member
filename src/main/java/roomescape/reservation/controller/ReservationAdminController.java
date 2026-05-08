package roomescape.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.dto.ReservationsResponse;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/admin/reservations")
public class ReservationAdminController {
    private final ReservationService reservationService;

    public ReservationAdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<ReservationsResponse> list() {
        return ResponseEntity.ok(
                ReservationsResponse.from(reservationService.findAll()
                        .stream()
                        .map(ReservationResponse::from)
                        .toList()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
