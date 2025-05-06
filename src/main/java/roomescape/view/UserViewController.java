package roomescape.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {
    @GetMapping("/reservation")
    public String showUserReservation() {
        return "reservation";
    }

    @GetMapping
    public String showMain() {
        return "index";
    }
}
