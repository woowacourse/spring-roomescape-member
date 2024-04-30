package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class LoadController {
    @GetMapping
    public String loadAdminPage() {
        return "/admin/index";
    }

    @GetMapping("/reservation")
    public String loadReservationPage() {
        return "/admin/reservation";
    }

    @GetMapping("/time")
    public String loadTimePage() {
        return "admin/time";
    }
}
