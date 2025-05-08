package roomescape.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping
    public String showTopRankedTheme() {
        return "index";
    }

    @GetMapping("/reservation")
    public String showReservation() {
        return "reservation";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }
}
