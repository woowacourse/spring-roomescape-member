package roomescape.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.service.ReservationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        final List<ReservationResponse> reservationResponses = reservationService.getReservations();
        return ResponseEntity.ok(reservationResponses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> saveReservation(
            @RequestBody final ReservationSaveRequest reservationSaveRequest) {
        final ReservationResponse reservationResponse = reservationService.saveReservation(reservationSaveRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(final @PathVariable("id") Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
