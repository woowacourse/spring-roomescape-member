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

    @GetMapping("time")
    public String time() {
        return "admin/time";
    }
}
