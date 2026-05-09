package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }

    @GetMapping("/admin/reservations")
    public String adminReservations() {
        return "admin/reservations";
    }

    @GetMapping("/admin/times")
    public String adminTimes() {
        return "admin/times";
    }

    @GetMapping("/admin/themes")
    public String adminThemes() {
        return "admin/themes";
    }
}
