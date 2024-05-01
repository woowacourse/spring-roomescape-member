package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/reservation")
    public String reservationPage() {
        return "reservation";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    public String adminReservationPage() {
        return "admin/reservation-new";
    }

    @GetMapping("/admin/time")
    public String adminTimePage() {
        return "admin/time";
    }

    @GetMapping("/admin/theme")
    public String adminThemePage() {
        return "admin/theme";
    }
}
