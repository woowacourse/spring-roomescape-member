package roomescape.controller.viewcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {
    @GetMapping
    public String findAdminPage() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String findAdminReservationPage() {
        return "admin/reservation-new";
    }

    @GetMapping("/time")
    public String findAdminTimePage() {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String findAdminThemePage() {
        return "admin/theme";
    }
}
