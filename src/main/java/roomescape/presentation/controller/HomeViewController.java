package roomescape.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeViewController {
    @GetMapping("/")
    public String redirectReservationPage() {
        return "index";
    }

    @GetMapping("/reservation")
    public String reservationPage() {
        return "reservation";
    }
}
