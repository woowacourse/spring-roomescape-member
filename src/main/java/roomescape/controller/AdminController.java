package roomescape.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.Member;
import roomescape.dto.AdminReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.service.ReservationService;


@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createAdminReservation(
            @RequestBody AdminReservationRequest adminReservationRequest,
            Member member
    ) {
        Long id = reservationService.addReservation(adminReservationRequest, member);
        ReservationResponse reservation = reservationService.getReservation(id);
        return ResponseEntity.created(URI.create("/reservations/" + id)).body(reservation);
    }
}
