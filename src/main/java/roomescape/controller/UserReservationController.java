package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/reservation")
public class UserReservationController {
    @GetMapping
    public String readUserReservation() {
        return "reservation";
    }
}
