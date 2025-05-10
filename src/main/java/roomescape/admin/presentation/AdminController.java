package roomescape.admin.presentation;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.presentation.dto.AdminReservationRequest;
import roomescape.reservation.application.service.ReservationService;
import roomescape.reservation.presentation.dto.ReservationResponse;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(
            final @RequestBody @Valid AdminReservationRequest request
    ) {
        ReservationResponse reservation = reservationService.createReservation(request);
        return ResponseEntity.ok().body(reservation);
    }

}
