package roomescape.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {
    @GetMapping
    public String adminPage() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String reservationAdminPage() {
        return "admin/reservation";
    }

    @GetMapping("/time")
    public String timeAdminPage() {
        return "admin/time";
    }
}
