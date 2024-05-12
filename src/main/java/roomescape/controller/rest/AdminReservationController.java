package roomescape.controller.rest;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.reservation.request.AdminReservationRequest;
import roomescape.dto.reservation.request.UserReservationRequest;
import roomescape.dto.reservation.response.ReservationResponse;
import roomescape.service.ReservationService;

import java.net.URI;

@RestController
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody @Valid AdminReservationRequest reservationRequest) {
        UserReservationRequest request = new UserReservationRequest(
                reservationRequest.date(),
                reservationRequest.timeId(),
                reservationRequest.themeId()
        );
        ReservationResponse reservationResponse = reservationService.createReservation(request, reservationRequest.memberId());

        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }
}
