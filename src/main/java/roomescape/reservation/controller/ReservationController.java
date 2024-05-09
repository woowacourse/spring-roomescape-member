package roomescape.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.principal.AuthenticatedMember;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.SaveReservationRequest;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.service.ReservationService;
import roomescape.resolver.Authenticated;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> getReservations(@Authenticated final AuthenticatedMember authenticatedMember) {
        return reservationService.getReservations()
                .stream()
                .map(reservation -> ReservationResponse.from(reservation, authenticatedMember))
                .toList();
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> saveReservation(
            @RequestBody final SaveReservationRequest request,
            @Authenticated final AuthenticatedMember authenticatedMember
    ) {
        final Reservation savedReservation = reservationService.saveReservation(request);

        return ResponseEntity.created(URI.create("/reservations/" + savedReservation.getId()))
                .body(ReservationResponse.from(savedReservation, authenticatedMember));
    }

    @DeleteMapping("/reservations/{reservation-id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("reservation-id") final Long reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.noContent().build();
    }
}
