package roomescape.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/admin")
    public String home() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String reservations() {
        return "reservation";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/admin/reservation")
    public String adminReservation() {
        return "admin/reservation-new";
    }

    @GetMapping("/admin/time")
    public String adminTime() {
        return "admin/time";
    }

    @GetMapping("/admin/theme")
    public String adminTheme() {
        return "admin/theme";
    }
}
