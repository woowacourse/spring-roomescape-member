package roomescape.admin.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.dto.AdminReservationRequest;
import roomescape.global.annotation.Auth;
import roomescape.member.role.MemberRole;
import roomescape.reservation.service.ReservationService;

@RestController
public class AdminController {

    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Auth(role = MemberRole.ADMIN)
    @PostMapping("/admin/reservations")
    public ResponseEntity<Void> reservationSave(@RequestBody AdminReservationRequest adminReservationRequest) {
        reservationService.addAdminReservation(adminReservationRequest);

        return ResponseEntity.created(URI.create("/admin/reservations/" + adminReservationRequest.memberId()))
                .build();
    }
}
