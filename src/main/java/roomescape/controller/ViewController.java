package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/admin")
    public String getAdminHomePage() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    public String getAdminReservationPage() {
        return "admin/reservation-new";
    }

    @GetMapping("/admin/time")
    public String getAdminTimePage() {
        return "admin/time";
    }

    @GetMapping("/admin/theme")
    public String getAdminThemePage() {
        return "admin/theme";
    }

    @GetMapping("/reservation")
    public String getReservationPage() {
        return "reservation";
    }

    @GetMapping()
    public String getHomePage() {
        return "index";
    }
}
