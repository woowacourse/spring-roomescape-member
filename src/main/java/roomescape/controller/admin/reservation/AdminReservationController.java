package roomescape.controller.admin.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.domain.Reservation;

@Controller
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    @PostMapping
    public ResponseEntity<Reservation> addReservation() {
        return ResponseEntity.noContent().build();
    }
}
