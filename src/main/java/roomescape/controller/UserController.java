package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping
    public String showHomePage() {
        return "/index";
    }

    @GetMapping("/reservation")
    public String showReservationPage() {
        return "/reservation";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "/login";
    }
}
