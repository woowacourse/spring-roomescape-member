package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.request.CreateReservationRequest;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.response.CreateReservationResponse;
import roomescape.reservation.service.ReservationService;
import roomescape.user.domain.UserPrincipal;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<CreateReservationResponse> addReservation(
            @RequestBody @Valid ReservationRequest reservationRequest, UserPrincipal userPrincipal) {
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                reservationRequest.date(),
                reservationRequest.themeId(),
                reservationRequest.timeId(),
                userPrincipal.id()
        );
        CreateReservationResponse newReservation = reservationService.addReservation(createReservationRequest);
        Long id = newReservation.id();
        return ResponseEntity.created(URI.create("/reservations/" + id)).body(newReservation);
    }
}
