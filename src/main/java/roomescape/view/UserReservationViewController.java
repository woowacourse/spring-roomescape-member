package roomescape.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserReservationViewController {

    @GetMapping
    public String showMain() {
        return "index";
    }

    @GetMapping("/reservation")
    public String showReservations() {
        return "reservation";
    }
}
