package roomescape.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/admin")
    public String adminHome() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    public String adminReservation() {
        return "admin/reservations";
    }

    @GetMapping("/admin/time")
    public String adminTime() {
        return "admin/times";
    }

    @GetMapping("/admin/theme")
    public String adminTheme() {
        return "admin/themes";
    }
}
