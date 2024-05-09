package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.AdminReservationRequest;
import roomescape.dto.response.AdminReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<AdminReservationResponse> addReservation(
            @RequestBody @Valid AdminReservationRequest adminReservationRequest
    ) {
        AdminReservationResponse adminReservationResponse = reservationService.addReservation(adminReservationRequest);

        return ResponseEntity.created(URI.create("/reservations/" + adminReservationResponse.id()))
                .body(adminReservationResponse);
    }
}
