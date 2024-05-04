package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String readAdminPage() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    public String readAdminReservationPage() {
        return "admin/reservation-new";
    }

    @GetMapping("/admin/time")
    public String readAdminTimePage() {
        return "admin/time";
    }

    @GetMapping("/admin/theme")
    public String readAdminThemePage() {
        return "admin/theme";
    }
}
