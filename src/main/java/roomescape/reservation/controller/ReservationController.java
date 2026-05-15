package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.controller.dto.CreateReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.controller.dto.UpdateReservationRequest;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.RescheduleReservationInfo;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<ReservationResponse> reservations = reservationService.findAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping(params = "name")
    public ResponseEntity<List<ReservationResponse>> getReservations(@RequestParam String name) {
        List<ReservationResponse> reservations = reservationService.findAllReservationsByName(name);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody CreateReservationRequest createReservationRequest) {
        ReservationResponse reservation = reservationService.reserve(createReservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservation);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReservationResponse> updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReservationRequest updateReservationRequest) {
        ReservationResponse reservation = reservationService.rescheduleReservation(
                RescheduleReservationInfo.of(id, updateReservationRequest));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservation);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}

