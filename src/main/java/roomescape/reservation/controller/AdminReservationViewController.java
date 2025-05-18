package roomescape.reservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminReservationViewController {

    @GetMapping("/reservation")
    public String reservation() {
        return "admin/reservation-new";
    }

}
