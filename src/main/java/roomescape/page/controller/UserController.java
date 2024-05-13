package roomescape.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping
    public String getAdminPageByRedirect() {
        return "index";
    }

    @GetMapping("/reservation")
    public String getReservationPage() {
        return "/reservation";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "/login";
    }
}
