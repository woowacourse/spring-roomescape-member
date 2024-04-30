package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping
    public String mainPage() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String reservationPage() {
        return "admin/reservation-new";
    }

    @GetMapping("/time")
    public String reservationTimePage() {
        return "admin/time";
    }
}
