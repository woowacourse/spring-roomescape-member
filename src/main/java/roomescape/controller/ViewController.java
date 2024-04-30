package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/admin")
    public String mainPage() {
        return "admin/index";
    }

    @GetMapping("/admin/time")
    public String reservationTimeAdminPage() {
        return "/admin/time";
    }

    @GetMapping("/admin/reservation")
    public String reservationPage() {
        return "admin/reservation";
    }
}
