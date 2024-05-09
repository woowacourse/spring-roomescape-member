package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String readAdminPage() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String readAdminReservationPage() {
        return "admin/reservation-new";
    }

    @GetMapping("/time")
    public String readAdminTimePage() {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String readAdminThemePage() {
        return "admin/theme";
    }
}
