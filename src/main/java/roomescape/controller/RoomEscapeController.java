package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class RoomEscapeController {

    @GetMapping
    public String dashboard() {
        return "admin/index";
    }

    @GetMapping("/time")
    public String adminReservationTimeDashboard() {
        return "admin/time";
    }

    @GetMapping("/reservation")
    public String adminReservationDashboard() {
        return "admin/reservation";
    }
}
