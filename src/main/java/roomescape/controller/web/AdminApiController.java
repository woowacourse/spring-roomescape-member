package roomescape.controller.web;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.AdminReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/admin")
public class AdminApiController {
    private final ReservationService reservationService;

    public AdminApiController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> saveAdminReservation(@RequestBody @Valid AdminReservationRequest reservationRequest) {
        ReservationResponse reservationResponse = reservationService.save(reservationRequest);
        return ResponseEntity.ok(reservationResponse);
    }
}
