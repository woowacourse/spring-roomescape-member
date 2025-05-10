package roomescape.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {
    @GetMapping("/reservation")
    public String reservationNew() {
        return "/reservation";
    }

    @GetMapping("/login")
    public String userLogin() {
        return "/login";
    }

    @GetMapping
    public String home() {
        return "/index";
    }
}
