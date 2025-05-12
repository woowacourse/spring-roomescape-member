package roomescape.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminViewController {

    @GetMapping
    public String admin() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String adminReservation() {
        return "admin/reservation-new";
    }

    @GetMapping("/time")
    public String adminTime() {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String adminTheme() {
        return "admin/theme";
    }
}
