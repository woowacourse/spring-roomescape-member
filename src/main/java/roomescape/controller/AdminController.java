package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

import java.net.URI;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ReservationService reservationService;

    public AdminController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createAdminReservation(@RequestBody final ReservationRequest request) {
        ReservationResponse reservationResponse = reservationService.create(request);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id())).body(reservationResponse);
    }
}
