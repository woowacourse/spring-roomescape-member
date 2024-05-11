package roomescape.controller.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import roomescape.controller.RequestParams;
import roomescape.controller.login.LoginMember;
import roomescape.service.reservation.ReservationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations(@RequestParams SearchReservationRequest request) {
        if (request == null || request.existNull()) {
            return ResponseEntity.ok(reservationService.getReservations());
        }
        return ResponseEntity.ok(reservationService.getReservations(request));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> addReservation(
            @RequestBody CreateReservationRequest request,
            LoginMember member) {
        CreateReservationRequest assignedMemberRequest = request.assignMemberId(member.id());
        ReservationResponse reservation = reservationService.addReservation(assignedMemberRequest);
        URI uri = UriComponentsBuilder.fromPath("/reservations/{id}")
                .buildAndExpand(reservation.id())
                .toUri();

        return ResponseEntity.created(uri)
                .body(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservations(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
