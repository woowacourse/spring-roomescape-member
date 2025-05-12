package roomescape.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping()
    public String home() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String getAdminReservationPage() {
        return "admin/reservation-new";
    }

    @GetMapping("/time")
    public String getReservationTimePage() {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String getThemePage() {
        return "admin/theme";
    }
}
