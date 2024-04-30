package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminController {

    @GetMapping
    public String mainPage() {
        return "/admin/index";
    }

    @GetMapping("/reservation")
    public String reservationPage() {
        return "/admin/reservation";
    }

    @GetMapping("/time")
    public String timePate() {
        return "/admin/time";
    }
}
