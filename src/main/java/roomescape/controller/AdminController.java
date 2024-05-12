package roomescape.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationAdminCreateRequest;
import roomescape.dto.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservationByAdmin(
            @RequestBody ReservationAdminCreateRequest reservationAdminCreateRequest
    ) {
        ReservationResponse reservationResponse = reservationService.createByAdmin(reservationAdminCreateRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }
}
