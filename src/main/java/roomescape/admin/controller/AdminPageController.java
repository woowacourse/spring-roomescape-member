package roomescape.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

    @GetMapping("/admin")
    public String adminPage() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    public String reservationPage() {
        return "admin/reservation-new";
    }

    @GetMapping("/admin/time")
    public String timePage() {
        return "admin/time";
    }

    @GetMapping("/admin/theme")
    public String themePage() { return  "admin/theme"; }
}
