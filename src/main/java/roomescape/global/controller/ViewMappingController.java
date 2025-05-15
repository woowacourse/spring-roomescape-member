package roomescape.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewMappingController {

    @GetMapping
    public String home() {
        return "index";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    public String adminReservation() {
        return "admin/reservation-new";
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "/reservation";
    }

    @GetMapping("/admin/time")
    public String time() {
        return "admin/time";
    }

    @GetMapping("/admin/theme")
    public String theme() {
        return "admin/theme";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/admin/signup")
    public String adminSignup() {
        return "admin/signup";
    }
}
