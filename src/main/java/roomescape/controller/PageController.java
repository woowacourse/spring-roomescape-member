package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/admin")
    public String adminPage() {
        return "forward:/admin.html";
    }

    @GetMapping("/reservations")
    public String reservationPage() {
        return "forward:/reservations.html";
    }
}
