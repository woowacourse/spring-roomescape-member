package roomescape.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminViewController {

    @GetMapping("/admin")
    public String admin() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    public String adminReservation() {
        return "admin/reservation-new";
    }

    @GetMapping("/admin/time")
    public String adminTime() {
        return "admin/time";
    }

    @GetMapping("/admin/theme")
    public String adminTheme() {
        return "admin/theme";
    }
}
