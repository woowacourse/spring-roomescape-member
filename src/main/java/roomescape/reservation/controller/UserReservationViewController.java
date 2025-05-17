package roomescape.reservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserReservationViewController {

    @GetMapping("/reservation")
    public String userReservation() {
        return "reservation";
    }

}
