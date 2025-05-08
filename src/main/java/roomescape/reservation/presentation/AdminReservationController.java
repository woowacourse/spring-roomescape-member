package roomescape.reservation.presentation;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.global.resolver.LoginMember;
import roomescape.reservation.application.service.ReservationService;
import roomescape.reservation.presentation.dto.AdminReservationRequest;
import roomescape.reservation.presentation.dto.ReservationResponse;

@RestController
@RequestMapping("/admin")
public class AdminReservationController {
    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(
            final @RequestBody @Valid AdminReservationRequest adminReservationRequest, LoginMember member
    ) {
        ReservationResponse reservation = reservationService.createReservation(adminReservationRequest);

        return ResponseEntity.created(createUri(reservation.getId()))
                .body(reservation);
    }

    private URI createUri(Long reservationId) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservationId)
                .toUri();
    }
}
