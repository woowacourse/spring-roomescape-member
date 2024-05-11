package roomescape.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.config.LoggedIn;
import roomescape.domain.AuthenticatedMember;
import roomescape.service.dto.reservation.ReservationCreateRequest;
import roomescape.service.dto.reservation.ReservationResponse;
import roomescape.service.ReservationService;

import java.util.List;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> readReservations() {
        return reservationService.readReservations();
    }

    @GetMapping("/reservations/{id}")
    public ReservationResponse readReservation(@PathVariable Long id) {
        return reservationService.readReservation(id);
    }

    @PostMapping("/reservations")
    public ReservationResponse createReservation(
            @RequestBody ReservationCreateRequest request,
            @LoggedIn AuthenticatedMember member
    ) {
        ReservationCreateRequest requestByMember = request.withMemberId(member.getId());
        return reservationService.createReservation(requestByMember);
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> createReservationByAdmin(@RequestBody ReservationCreateRequest requestByAdmin) {
        ReservationResponse response = reservationService.createReservation(requestByAdmin);
        URI location = URI.create("/reservations/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/reservations/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }
}
