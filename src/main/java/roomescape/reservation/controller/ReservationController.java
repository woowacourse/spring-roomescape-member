package roomescape.reservation.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.resolver.AuthenticationPrincipal;
import roomescape.member.domain.Member;
import roomescape.reservation.controller.dto.CreateReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.CreateReservationServiceRequest;

@RequestMapping("/reservations")
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse addReservation(
            @RequestBody CreateReservationRequest request,
            @AuthenticationPrincipal Member member) {

        final CreateReservationServiceRequest creation =
                CreateReservationServiceRequest.fromRequestAndMember(request, member);
        Reservation savedReservation = reservationService.addReservation(creation);

        return ReservationResponse.from(savedReservation);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponse> findAllReservations() {
        return reservationService.findAllReservations()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeReservation(@PathVariable long id) {
        reservationService.removeReservationById(id);
    }
}
