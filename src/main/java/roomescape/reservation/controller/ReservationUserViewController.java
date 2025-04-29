package roomescape.reservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservationUserViewController {

    @GetMapping("admin")
    public String home() {
        return "admin/index";
    }

    @GetMapping("admin/reservation")
    public String reservation() {
        return "admin/reservation";
    }

    @GetMapping("admin/time")
    public String adminTime() {
        return "admin/time";
    }
}
