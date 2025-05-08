package roomescape.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @GetMapping
    public String getMainPage() {
        return "index";
    }

    @GetMapping("/reservation")
    public String getReservationPage() {
        return "reservation";
    }
}
