package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberReservationController { // TODO: 네이밍 애매함

    @GetMapping("/reservation")
    public String reservationPage() {
        return "/reservation";
    }

    @GetMapping
    public String homePage() {
        return "index";
    }
}
