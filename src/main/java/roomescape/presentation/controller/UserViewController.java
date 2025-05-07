package roomescape.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @GetMapping("/reservation")
    public String reservations() {
        return "reservation";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
