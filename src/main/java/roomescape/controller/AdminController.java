package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String getMainPage() {
        return "index";
    }

    @GetMapping("/admin/reservation")
    public String getReservationPage() {
        return "reservation";
    }

    @GetMapping("/admin/time")
    public String getReservationTimePage() {
        return "time";
    }

    @GetMapping("/admin/theme")
    public String getThemePage() {
        return "theme";
    }
}
