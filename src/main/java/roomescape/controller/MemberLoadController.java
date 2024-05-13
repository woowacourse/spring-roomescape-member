package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberLoadController {
    @GetMapping("/reservation")
    public String loadUserReservationPage() {
        return "/reservation";
    }

    @GetMapping("/login")
    public String loadLoginPage() {
        return "/login";
    }

    @GetMapping("/signup")
    public String loadSignUpPage() {
        return "/signup";
    }
}
