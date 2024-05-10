package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

    @GetMapping
    public String showUserIndexPage() {
        return "index";
    }

    @GetMapping("/reservation")
    public String showUserReservationPage() {
        return "reservation";
    }

    @GetMapping("/login")
    public String showUserLoginPage() {
        return "login";
    }
}
