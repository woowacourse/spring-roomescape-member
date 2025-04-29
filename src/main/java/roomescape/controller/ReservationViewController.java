package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class ReservationViewController {

    @GetMapping
    public String showAdminWelcome() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String showReservations() {
        return "admin/reservation";
    }

    @GetMapping("/time")
    public String showTimes() {
        return "admin/time";
    }
}
