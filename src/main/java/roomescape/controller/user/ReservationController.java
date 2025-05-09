package roomescape.controller.user;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.annotation.AuthenticationPrinciple;
import roomescape.domain.Reservation;
import roomescape.dto.other.AuthenticationInformation;
import roomescape.dto.request.ReservationCreationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(
            @AuthenticationPrinciple AuthenticationInformation authenticationInformation,
            @Valid @RequestBody ReservationCreationRequest request
    ) {
        long id = reservationService.saveReservation(authenticationInformation.id(), request);
        Reservation savedReservation = reservationService.getReservationById(id);
        return ResponseEntity
                .created(URI.create("/reservations/" + id))
                .body(new ReservationResponse(savedReservation));
    }
}
