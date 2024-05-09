package roomescape.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {

    @GetMapping("/")
    public String showPopularThemePage() {
        return "index";
    }

    @GetMapping("/reservation")
    public String showReservationPage() {
        return "reservation";
    }
}
