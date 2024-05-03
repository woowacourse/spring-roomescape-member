package roomescape.reservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservationPageController {

    @GetMapping("/reservation")
    public String userReservationPage() {
        return "reservation";
    }

    @GetMapping("/")
    public String popularPage() {
        return "index";
    }
}

