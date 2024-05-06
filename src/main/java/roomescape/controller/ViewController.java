package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String popularThemePage() {
        return "index";
    }

    @GetMapping("/reservation")
    public String reservationPage() {
        return "reservation";
    }
}
