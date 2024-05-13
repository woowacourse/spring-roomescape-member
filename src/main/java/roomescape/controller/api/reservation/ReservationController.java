package roomescape.controller.api.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.AuthenticatedUser;
import roomescape.dto.request.ReservationAddMemberRequest;
import roomescape.dto.request.ReservationAddRequest;
import roomescape.dto.response.AuthResponse;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.MemberService;
import roomescape.service.ReservationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final MemberService memberService;

    public ReservationController(ReservationService reservationService, MemberService memberService) {
        this.reservationService = reservationService;
        this.memberService = memberService;
    }

    @GetMapping
    public List<ReservationResponse> getReservations() {
        return reservationService.findReservations();
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> addReservation(
            @RequestBody ReservationAddRequest reservationAddRequest,
            @AuthenticatedUser AuthResponse authResponse
            ) {
        MemberResponse memberResponse = memberService.findMemberByEmail(authResponse.email());
        ReservationAddMemberRequest reservationAddMemberRequest = new ReservationAddMemberRequest(memberResponse);
        ReservationResponse reservationResponse = reservationService.addReservation(reservationAddRequest, reservationAddMemberRequest);

        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
