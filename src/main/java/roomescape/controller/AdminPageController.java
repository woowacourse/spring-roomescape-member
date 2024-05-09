package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminPageController {
    @GetMapping("/admin")
    public String mainPage() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    public String reservationPage() {
        return "admin/reservation-new";
    }

    @GetMapping("/admin/time")
    public String reservationTimePage() {
        return "admin/time";
    }

    @GetMapping("/admin/theme")
    public String themePage() {
        return "admin/theme";
    }
}
