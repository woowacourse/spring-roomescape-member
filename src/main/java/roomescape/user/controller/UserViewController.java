package roomescape.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @GetMapping("/reservation")
    public String userReservationView() {
        return "reservation";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
