package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/admin")
    public String adminIndexPage() {
        return "admin/index";
    }

    @GetMapping("/admin/time")
    public String adminTimePage() {
        return "admin/time";
    }

    @GetMapping("/admin/reservation")
    public String adminReservationPage() {
        return "admin/reservation-new";
    }

    @GetMapping("/admin/theme")
    public String adminThemePage() {
        return "admin/theme";
    }

    @GetMapping("/reservation")
    public String reservationPage() {
        return "reservation";
    }
}
