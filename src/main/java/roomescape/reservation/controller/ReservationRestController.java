package roomescape.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationSaveRequest;
import roomescape.reservation.service.ReservationService;
import roomescape.global.auth.AuthUser;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationRestController {

    private final ReservationService reservationService;

    public ReservationRestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<ReservationResponse> responses = reservationService.findAllReservations();

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationSaveRequest request,
                                                                 AuthUser authUser) {
        ReservationResponse response = reservationService.saveReservation(request, authUser);

        URI location = URI.create("/reservations/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservationById(id);

        return ResponseEntity.noContent().build();
    }
}
