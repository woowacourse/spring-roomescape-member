package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.security.Permission;
import roomescape.domain.Reservation;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.service.ReservationService;
import roomescape.service.dto.ReservationResponse;
import roomescape.service.dto.ReservationSaveRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@Permission(role = Role.ADMIN)
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationFindService) {
        this.reservationService = reservationFindService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<ReservationResponse> reservationResponses = reservationService.findReservations().stream()
                .map(ReservationResponse::of)
                .toList();
        return ResponseEntity.ok(reservationResponses);
    }

    @PostMapping
    @Permission(role = Role.MEMBER)
    public ResponseEntity<ReservationResponse> addReservation(
            @RequestBody @Valid ReservationSaveRequest request,
            Member member) {
        Reservation newReservation = reservationService.createReservation(request, member);
        return ResponseEntity.created(URI.create("/reservations/" + newReservation.getId()))
                .body(ReservationResponse.of(newReservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
