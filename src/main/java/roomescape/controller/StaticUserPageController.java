package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticUserPageController {

    @GetMapping("/reservation")
    public String getReservation() {
        return "reservation";
    }
}
