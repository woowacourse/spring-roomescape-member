package roomescape.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/admin")
    public String admin() {
        return "/admin/index";
    }

    @GetMapping("/admin/reservation")
    public String adminReservation() {
        return "/admin/reservation-new";
    }

    @GetMapping("/admin/time")
    public String adminTime() {
        return "/admin/time";
    }

    @GetMapping("/admin/theme")
    public String adminTheme() {
        return "/admin/theme";
    }
}
