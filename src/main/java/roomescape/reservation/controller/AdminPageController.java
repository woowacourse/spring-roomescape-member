package roomescape.reservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    @GetMapping
    public String admin() {
        return "/admin/index";
    }

    @GetMapping("/reservation")
    public String getReservation() {
        return "admin/reservation-new";
    }

    @GetMapping("/theme")
    public String getTheme() {
        return "admin/theme";
    }

    @GetMapping("/time")
    public String time() {
        return "admin/time";
    }
}
