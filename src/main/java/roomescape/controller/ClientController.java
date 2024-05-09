package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {

    @GetMapping
    public String readPopularThemePage() {
        return "index";
    }

    @GetMapping("/reservation")
    public String readReservationPage() {
        return "reservation";
    }
}
