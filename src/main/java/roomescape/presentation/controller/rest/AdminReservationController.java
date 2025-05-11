package roomescape.presentation.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.domain.LoginUser;
import roomescape.business.domain.Role;
import roomescape.business.service.ReservationService;
import roomescape.exception.auth.UnauthorizedAccessException;
import roomescape.presentation.AuthenticatedUser;
import roomescape.presentation.dto.reservation.ReservationResponse;
import roomescape.presentation.dto.reservation.AdminReservationRequest;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            @AuthenticatedUser final LoginUser loginUser,
            @RequestBody final AdminReservationRequest request
    ) {
        final ReservationResponse reservationResponse = reservationService.create(
                loginUser.id(),
                request.toReservationRequest()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(reservationResponse);
    }
}
