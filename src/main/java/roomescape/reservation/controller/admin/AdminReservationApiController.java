package roomescape.reservation.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.admin.AdminReservationRequest;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationApiController {
    private final ReservationService reservationService;

    public AdminReservationApiController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> add(@RequestBody AdminReservationRequest adminReservationRequest) {
        ReservationResponse reservationResponse = reservationService.addByAdmin(adminReservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationResponse);
    }
}
