package roomescape.reservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/reservation")
public class AdminReservationViewController {

    @GetMapping
    public String getAdminReservation() {
        return "admin/reservation-new";
    }
}
