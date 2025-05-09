package roomescape.member.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    @GetMapping("/reservation")
    public String showReservation() {
        return "reservation";
    }

    @GetMapping
    public String showWeeklyTop10PopularTheme() {
        return "index";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }
}
