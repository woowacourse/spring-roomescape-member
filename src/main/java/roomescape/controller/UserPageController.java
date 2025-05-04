package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

    @GetMapping
    public String displayThemeRank() {
        return "/index";
    }

    @GetMapping("/reservation")
    public String displayReservation() {
        return "reservation";
    }
}
