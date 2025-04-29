package roomescape.admin;

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
        return "admin/reservation";
    }

    @GetMapping("/time")
    public String showAllTime() {
        return "admin/time";
    }
}
