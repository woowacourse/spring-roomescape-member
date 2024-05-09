package roomescape.controller.reservation;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import roomescape.controller.member.dto.CreateReservationRequest;
import roomescape.controller.reservation.dto.ReservationResponse;
import roomescape.service.ReservationService;

import java.net.URI;

@RestController
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> addReservation(
            @RequestBody @Valid final CreateReservationRequest request) {

        final ReservationResponse reservation = reservationService.addReservationAdmin(request);
        final URI uri = UriComponentsBuilder.fromPath("/reservations/{id}")
                .buildAndExpand(reservation.id())
                .toUri();

        return ResponseEntity.created(uri)
                .body(reservation);
    }
}
