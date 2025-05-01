package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewMappingController {

    @GetMapping("/admin")
    public String admin() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    public String adminReservation() {
        return "admin/reservation-new";
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "/reservation";
    }

    @GetMapping("/admin/time")
    public String time() {
        return "admin/time";
    }

    @GetMapping("/admin/theme")
    public String theme() {
        return "admin/theme";
    }
}
