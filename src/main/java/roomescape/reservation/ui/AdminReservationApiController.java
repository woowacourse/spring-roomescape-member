package roomescape.reservation.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.AdminReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.application.ReservationService;

@RestController
public class AdminReservationApiController {

    private final ReservationService reservationService;

    public AdminReservationApiController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> addByAdmin(@RequestBody AdminReservationRequest adminReservationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.addByAdmin(adminReservationRequest));
    }
}
