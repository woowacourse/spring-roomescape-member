package roomescape.reservation.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.resolver.AuthenticationPrincipal;
import roomescape.member.domain.Member;
import roomescape.reservation.controller.dto.CreateAdminReservationRequest;
import roomescape.reservation.controller.dto.CreateUserReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.CreateReservationServiceRequest;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse addReservation(
            @RequestBody CreateUserReservationRequest request,
            @AuthenticationPrincipal Member member) {

        final CreateReservationServiceRequest creation = request.toCreateReservationServiceRequest(member);
        final Reservation savedReservation = reservationService.addReservation(creation);

        return ReservationResponse.from(savedReservation);
    }

    @PostMapping("admin/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse addReservation(
            @RequestBody CreateAdminReservationRequest request) {

        final CreateReservationServiceRequest serviceRequest = request.toCreateReservationServiceRequest();
        final Reservation savedReservation = reservationService.addReservation(serviceRequest);

        return ReservationResponse.from(savedReservation);
    }

    @GetMapping("/reservations")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponse> findReservations(
            @RequestParam(required = false) Long themeId,
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo
    ) {
        return reservationService.findByConditions(memberId, themeId, dateFrom, dateTo)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @DeleteMapping("/reservations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeReservation(@PathVariable long id) {
        reservationService.removeReservationById(id);
    }
}
