package roomescape.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;
import roomescape.member.domain.Member;
import roomescape.member.service.AuthService;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.AdminReservationRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.service.ReservationService;
import java.net.URI;

@Controller
@RequestMapping("/admin/reservations")
public class ReservationAdminController {

    private final ReservationService reservationService;
    private final AuthService authService;

    public ReservationAdminController(ReservationService reservationService, AuthService authService) {
        this.reservationService = reservationService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> postReservation(@RequestBody AdminReservationRequest adminReservationRequest) {
        Member member = authService.findById(adminReservationRequest.memberId());
        Reservation reservation = reservationService.createReservation(member, adminReservationRequest.toReservationRequest());
        URI location = UriComponentsBuilder.newInstance()
                .path("/reservations/{id}")
                .buildAndExpand(reservation.getId())
                .toUri();

        return ResponseEntity.created(location).body(ReservationResponse.from(reservation));
    }
}
