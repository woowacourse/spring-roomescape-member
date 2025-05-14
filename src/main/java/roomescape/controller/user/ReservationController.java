package roomescape.controller.user;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.annotation.AuthenticationPrinciple;
import roomescape.domain.Reservation;
import roomescape.dto.other.AuthenticationInformation;
import roomescape.dto.other.ReservationCreationContent;
import roomescape.dto.request.ReservationCreationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @AuthenticationPrinciple AuthenticationInformation authInformation,
            @Valid @RequestBody ReservationCreationRequest request
    ) {
        ReservationCreationContent content = new ReservationCreationContent(authInformation.id(), request);
        long id = reservationService.saveReservation(content);
        Reservation savedReservation = reservationService.getReservationById(id);
        return ResponseEntity
                .created(URI.create("/reservations/" + id))
                .body(new ReservationResponse(savedReservation));
    }
}
