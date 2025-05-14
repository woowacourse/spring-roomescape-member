package roomescape.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @GetMapping
    public String getHomePage() {
        return "index";
    }

    @GetMapping("/reservation")
    public String getReservationPage() {
        return "reservation";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String getSignUpPage() {
        return "/signup";
    }
}
