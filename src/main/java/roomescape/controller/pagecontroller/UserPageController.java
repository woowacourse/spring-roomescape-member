package roomescape.controller.pagecontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

    @GetMapping("/reservation")
    public String userReservation() {
        return "reservation";
    }

    @GetMapping
    public String bestThemes() {
        return "best-themes";
    }
}
