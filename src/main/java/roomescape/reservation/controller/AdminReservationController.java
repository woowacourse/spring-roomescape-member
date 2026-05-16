package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {
    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<Reservation> reservations = reservationService.findAll();
        List<ReservationResponse> response = reservations.stream()
                .map(ReservationResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createForceReservation(
            @Valid @RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = reservationService.save(
                reservationRequest.name(),
                reservationRequest.date(),
                reservationRequest.timeId(),
                reservationRequest.themeId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(ReservationResponse.from(reservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> forceDeleteReservation(@PathVariable long id) {
        reservationService.deleteByAdmin(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
