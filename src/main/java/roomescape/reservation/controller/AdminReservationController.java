package roomescape.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.controller.dto.ReservationListResponse;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/admin/reservations")
@RequiredArgsConstructor
public class AdminReservationController {
    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<ReservationListResponse> getAllReservations() {
        return ResponseEntity.ok(
                ReservationListResponse.from(reservationService.findAllReservations()
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
