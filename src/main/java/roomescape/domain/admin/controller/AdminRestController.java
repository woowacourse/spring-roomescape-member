package roomescape.domain.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.admin.dto.AdminReservationSaveRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.service.ReservationService;

import java.net.URI;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    private final ReservationService reservationService;

    public AdminRestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody AdminReservationSaveRequest request) {
        ReservationResponse response = reservationService.saveReservation(request);

        URI location = URI.create("/reservations/" + response.id());
        return ResponseEntity.created(location).body(response);
    }
}
