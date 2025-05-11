package roomescape.presentation.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.ReservationService;
import roomescape.presentation.dto.request.AdminReservationCreateRequest;
import roomescape.presentation.dto.response.ReservationResponse;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody @Valid AdminReservationCreateRequest request
    ) {
        ReservationResponse response = reservationService.createAdminReservation(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
