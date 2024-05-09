package roomescape.reservation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.global.annotation.LoginUser;
import roomescape.member.domain.Member;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;

@Controller
@RequestMapping
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String getPopularPage() {
        return "index";
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> reservations() {
        return ResponseEntity.ok(reservationService.findMemberReservations());
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(@LoginUser Member member,
                                                      @RequestBody @Valid ReservationRequest reservationRequest) {
        ReservationResponse response = reservationService.createMemberReservation(member, reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + response.memberReservationId())).body(response);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@LoginUser Member member,
                                       @PathVariable("id") @Min(1) long reservationMemberId) {
        reservationService.deleteMemberReservation(member, reservationMemberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservation")
    public String getReservationPage() {
        return "reservation";
    }
}
