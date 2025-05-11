package roomescape.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/reservation")
    public String showReservation() {
        return "reservation";
    }

    @GetMapping
    public String showWeeklyTop10PopularTheme() {
        return "index";
    }
}
