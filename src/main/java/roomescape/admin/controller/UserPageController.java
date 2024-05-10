package roomescape.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

    @GetMapping("/reservation")
    public String reservationPage() {
        return "reservation";
    }

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/login")
    public String logInPage() {
        return "login";
    }
}
