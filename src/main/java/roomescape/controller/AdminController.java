package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping
    public String findAdminPage() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String findAdminReservationPage() {
        return "admin/reservation";
    }

    @GetMapping("/time")
    public String findAdminReservationTimePage() {
        return "admin/time";
    }
}
