package roomescape.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.ReservationFilterRequest;
import roomescape.dto.ReservationWithMemberSaveRequest;
import roomescape.model.LoginMember;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.service.ReservationService;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getReservations(@ModelAttribute final ReservationFilterRequest reservationFilterRequest) {
        final List<ReservationResponse> reservationResponses = reservationService.getReservations(reservationFilterRequest);
        return ResponseEntity.ok(reservationResponses);
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> saveReservation(
            @RequestBody @Valid final ReservationWithMemberSaveRequest reservationWithMemberSaveRequest) {
        final ReservationResponse reservationResponse = reservationService.saveReservation(reservationWithMemberSaveRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> saveReservation(
            @RequestBody @Valid final ReservationSaveRequest reservationSaveRequest,
            final LoginMember loginMember) {
        final ReservationResponse reservationResponse = reservationService.saveReservation(reservationSaveRequest, loginMember);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(final @PathVariable("id") Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
