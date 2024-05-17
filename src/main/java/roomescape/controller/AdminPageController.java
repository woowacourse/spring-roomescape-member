package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.service.ReservationService;

@Controller
@RequestMapping("/admin")
public class AdminPageController {
    @GetMapping
    public String home() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "admin/reservation-new";
    }

    @GetMapping("/time")
    public String time() {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String theme() {
        return "admin/theme";
    }
}
