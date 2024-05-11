package roomescape.controller.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
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

    // TODO: RequestParams, QueryStringArgumentResolver 만들기 (https://growing-up-constantly.tistory.com/53)
    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations(
            @RequestParam(value = "themeId", required = false) final Long themeId,
            @RequestParam(value = "memberId", required = false) final Long memberId,
            @RequestParam(value = "dateFrom", required = false) final String dateFrom,
            @RequestParam(value = "dateTo", required = false) final String dateTo
    ) {
        final SearchReservationRequest request = new SearchReservationRequest(themeId, memberId, dateFrom, dateTo);

        if (request.existNull()) {
            return ResponseEntity.ok(reservationService.getReservations());
        }
        return ResponseEntity.ok(reservationService.getReservations(request));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> addReservation(@RequestBody final CreateReservationRequest request, LoginMember member) {
        final CreateReservationRequest assignedMemberRequest = request.assignMemberId(member.id());
        final ReservationResponse reservation = reservationService.addReservation(assignedMemberRequest);
        final URI uri = UriComponentsBuilder.fromPath("/reservations/{id}")
                .buildAndExpand(reservation.id())
                .toUri();

        return ResponseEntity.created(uri)
                .body(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservations(@PathVariable final Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
