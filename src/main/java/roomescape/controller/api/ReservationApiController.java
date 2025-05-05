package roomescape.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.ReservationRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.entity.Reservation;
import roomescape.service.ReservationService;

import java.util.List;

@RestController
public class ReservationApiController {

    private final ReservationService reservationService;

    public ReservationApiController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<Reservation> reservations = reservationService.getReservations();
        List<ReservationResponse> responses = ReservationResponse.from(reservations);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody @Valid ReservationRequest request) {
        Reservation reservation = reservationService.createReservation(request.name(), request.date(), request.timeId(), request.themeId());
        ReservationResponse response = ReservationResponse.from(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
