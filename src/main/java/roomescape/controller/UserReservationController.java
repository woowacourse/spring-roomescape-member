package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.service.ReservationService;

import java.net.URI;

@RestController
@RequestMapping("/user")
public class UserReservationController {

    private final ReservationService service;

    public UserReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> postReservation(@RequestBody ReservationRequest request) {
        ReservationResponse reservationResponse = service.postReservation(request);
        URI location = URI.create("/reservations/" + reservationResponse.id());
        return ResponseEntity.created(location).body(reservationResponse);
    }
}
