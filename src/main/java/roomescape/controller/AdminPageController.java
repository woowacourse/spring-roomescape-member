package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

    @GetMapping("/admin")
    public String admin() {
        return "/admin/index";
    }

    @GetMapping("/admin/reservation")
    public String getReservation() {
        return "admin/reservation-new";
    }

    @GetMapping("/admin/theme")
    public String getTheme() {
        return "admin/theme";
    }

    @GetMapping("/admin/time")
    public String time() {
        return "admin/time";
    }
}
