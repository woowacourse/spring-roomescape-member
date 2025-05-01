package roomescape.presentation.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.presentation.dto.request.ReservationRequest;
import roomescape.presentation.dto.response.ReservationResponse;
import roomescape.business.model.entity.Reservation;
import roomescape.business.service.ReservationService;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationApiController {

    private final ReservationService reservationService;

    public ReservationApiController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody @Valid ReservationRequest request) {
        Reservation reservation = reservationService.createReservation(request.name(), request.date(), request.timeId(), request.themeId());
        ReservationResponse response = ReservationResponse.from(reservation);
        return ResponseEntity.created(URI.create("/reservations")).body(response);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<Reservation> reservations = reservationService.getReservations();
        List<ReservationResponse> responses = ReservationResponse.from(reservations);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
