package roomescape.reservation.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.dto.ReservationCreateInfo;
import roomescape.reservation.dto.ReservationIdResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationsResponse;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationIdResponse> create(@RequestBody @Valid ReservationRequest request) {
        ReservationCreateInfo info = new ReservationCreateInfo(
                request.userId(), request.startAt(), request.themeId());
        ReservationIdResponse response = reservationService.create(info);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<ReservationsResponse> findAllByUserId(@RequestHeader("X-User-Id") @Valid Long id) {
        ReservationsResponse response = reservationService.findAllByUserId(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelMyReservation(
            @PathVariable Long id, @RequestHeader("X-User-Id") Long userId) {
        reservationService.cancel(id, userId);

        return ResponseEntity.noContent().build();
    }
}
