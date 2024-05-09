package roomescape.web.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ReservationService;
import roomescape.service.request.LoginMember;
import roomescape.service.request.MemberReservationRequest;
import roomescape.service.response.ReservationResponse;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody MemberReservationRequest request,
                                                                 LoginMember loginMember) {
        ReservationResponse response = reservationService.createReservation(request, loginMember);
        URI uri = URI.create("/reservations/" + response.id());

        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable long id) {
        reservationService.deleteReservation(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readReservations() {
        List<ReservationResponse> responses = reservationService.getAllReservations();

        return ResponseEntity.ok(responses);
    }

    @GetMapping(params = {"memberId", "themeId", "dateFrom", "dateTo"})
    public ResponseEntity<List<ReservationResponse>> findReservationsWithMemberThemePeriod(
            @RequestParam final Long memberId,
            @RequestParam final Long themeId,
            @RequestParam final LocalDate dateFrom,
            @RequestParam final LocalDate dateTo
    ) {
        List<ReservationResponse> responses =
                reservationService.findReservationsWithMemberThemePeriod(memberId, themeId, dateFrom, dateTo);

        return ResponseEntity.ok(responses);
    }
}
