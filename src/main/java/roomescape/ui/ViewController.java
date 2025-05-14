package roomescape.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping()
    public String index() {
        return "index";
    }

    @GetMapping("/admin")
    public String dashboard() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    public String adminReservationDashboard() {
        return "/admin/reservation-new";
    }

    @GetMapping("/admin/time")
    public String adminReservationTimeDashboard() {
        return "admin/time";
    }

    @GetMapping("/admin/theme")
    public String adminReservationThemeDashboard() {
        return "admin/theme";
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }
}
