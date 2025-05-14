package roomescape.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @GetMapping
    public String getAdminHomePage() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String getAdminReservationPage() {
        return "admin/reservation-new";
    }

    @GetMapping("/time")
    public String getAdminTimePage() {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String getAdminThemePage() {
        return "admin/theme";
    }
}
