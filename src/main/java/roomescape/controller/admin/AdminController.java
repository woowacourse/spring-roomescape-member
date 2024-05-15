package roomescape.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String showHomePage() {
        return "admin/index";
    }

    @GetMapping("/time")
    public String showTimePage() {
        return "admin/time";
    }

    @GetMapping("/reservation")
    public String showReservationPage() {
        return "admin/reservation-new";
    }

    @GetMapping("/theme")
    public String showThemePage() {
        return "admin/theme";
    }
}
