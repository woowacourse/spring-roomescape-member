package roomescape.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @GetMapping
    public String getAdmin() {
        return "redirect:/";
    }

    @GetMapping("/theme")
    public String getTheme() {
        return "admin/theme";
    }

    @GetMapping("/time")
    public String getTime() {
        return "admin/time";
    }

    @GetMapping("/reservation")
    public String getAdminReservation() {
        return "admin/reservation-new";
    }
}
