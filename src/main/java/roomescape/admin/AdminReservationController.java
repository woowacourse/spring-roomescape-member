package roomescape.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.domain.Visitor;
import roomescape.reservation.dto.ReservationCreateResponse;

@RestController
@RequestMapping("/admin")
public class AdminReservationController {

    private final AdminReservationService adminReservationService;

    public AdminReservationController(AdminReservationService adminReservationService) {
        this.adminReservationService = adminReservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationCreateResponse> create(Visitor visitor, @RequestBody AdminReservationRequest adminReservationRequest) {
        ReservationCreateResponse reservationCreateResponse = adminReservationService.create(visitor,
                adminReservationRequest);
        return ResponseEntity.ok(reservationCreateResponse);
    }
}
