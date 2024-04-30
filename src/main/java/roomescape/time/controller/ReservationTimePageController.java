package roomescape.time.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservationTimePageController {

    @GetMapping("/time")
    public String timePage() {
        return "admin/time";
    }
}
