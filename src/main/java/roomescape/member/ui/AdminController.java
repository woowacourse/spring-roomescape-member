package roomescape.member.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String showWelcome() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String showAllReservation() {
        return "admin/reservation-new";
    }

    @GetMapping("/time")
    public String showAllReservationTime() {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String showAllTheme() {
        return "admin/theme";
    }
}
