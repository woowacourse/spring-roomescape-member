package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservationPageController {
    @GetMapping("/reservation")
    public String reservation() {
        return "/reservation";
    }
}
