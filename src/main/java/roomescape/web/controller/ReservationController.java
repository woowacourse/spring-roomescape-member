package roomescape.web.controller;

import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ReservationService;
import roomescape.service.response.ReservationResponse;
import roomescape.web.controller.request.CreateReservationAdminRequest;
import roomescape.web.controller.request.CreateReservationWebRequest;
import roomescape.web.security.AuthenticatedMemberId;

@RestController
class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(
            @AuthenticatedMemberId Long id,
            @Valid @RequestBody CreateReservationWebRequest request
    ) {
        ReservationResponse response = reservationService.createReservation(request.toServiceRequest(id));
        URI uri = URI.create("/reservations/" + response.id());

        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> createAdminReservation(
            @Valid @RequestBody CreateReservationAdminRequest request
    ) {
        ReservationResponse response = reservationService.createReservation(request.toServiceRequest());
        URI uri = URI.create("/reservations/" + response.id());

        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> readReservations() {
        List<ReservationResponse> responses = reservationService.getAllReservations();

        return ResponseEntity.ok(responses);
    }
}
