package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping
    public String getHome() {
        return "index";
    }

    @GetMapping("/reservation")
    public String getReservation() {
        return "reservation";
    }

    @GetMapping("/admin")
    public String getAdminPage() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    public String getAdminReservationPage() {
        return "admin/reservation-new";
    }

    @GetMapping("/admin/time")
    public String getAdminReservationTimePage() {
        return "admin/time";
    }

    @GetMapping("/admin/theme")
    public String getAdminThemePage() {
        return "admin/theme";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }
}
