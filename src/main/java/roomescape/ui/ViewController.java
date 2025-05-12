package roomescape.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/reservation")
    public String userReservationView() {
        return "reservation";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
