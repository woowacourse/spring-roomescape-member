package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserViewController {

    @GetMapping("/reservation")
    public String userReservationView() {
        return "reservation";
    }

    @GetMapping("/login")
    public String userLoginView() {
        return "login";
    }
}
