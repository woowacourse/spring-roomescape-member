package roomescape.controller;

import org.springframework.web.bind.annotation.*;
import roomescape.config.LoggedIn;
import roomescape.domain.AuthenticatedMember;
import roomescape.service.dto.reservation.ReservationCreateRequest;
import roomescape.service.dto.reservation.ReservationResponse;
import roomescape.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationResponse> readReservations() {
        return reservationService.readReservations();
    }

    @GetMapping("/{id}")
    public ReservationResponse readReservation(@PathVariable Long id) {
        return reservationService.readReservation(id);
    }

    @PostMapping
    public ReservationResponse createReservation(
            @RequestBody ReservationCreateRequest request,
            @LoggedIn AuthenticatedMember member
    ) {
        ReservationCreateRequest requestOfMember = request.withMemberId(member.getId());
        return reservationService.createReservation(requestOfMember);
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }
}
