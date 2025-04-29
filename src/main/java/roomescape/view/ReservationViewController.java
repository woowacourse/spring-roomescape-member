package roomescape.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class ReservationViewController {

    @GetMapping
    public String showAdminWelcome() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String showReservations() {
        return "admin/reservation-new";
    }

    @GetMapping("/time")
    public String showTimes() {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String showThemes() {
        return "admin/theme";
    }
}
