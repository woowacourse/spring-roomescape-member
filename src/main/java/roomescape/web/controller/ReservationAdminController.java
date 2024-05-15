package roomescape.web.controller;

import java.net.URI;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ReservationService;
import roomescape.service.response.ReservationResponse;
import roomescape.web.controller.request.ReservationAdminWebRequest;

@RestController
@RequestMapping("/admin/reservations")
class ReservationAdminController {

    private final ReservationService reservationService;

    public ReservationAdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createAdminReservation(
            @Valid @RequestBody ReservationAdminWebRequest request
    ) {
        ReservationResponse response = reservationService.createReservation(request.toServiceRequest());
        URI uri = URI.create("/reservations/" + response.id());

        return ResponseEntity.created(uri).body(response);
    }
}
