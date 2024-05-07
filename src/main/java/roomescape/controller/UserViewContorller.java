package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewContorller {
    @GetMapping("/reservation")
    public String reservationUserPage() {
        return "reservation";
    }

    @GetMapping
    public String mainUserPage() {
        return "index";
    }
}
