package roomescape.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping
    public String getAdminPage() {
        return "/admin/index";
    }

    @GetMapping("/reservation")
    public String getAdminReservationPage() {
        return "/admin/reservation-new";
    }

    @GetMapping("/time")
    public String getTimePage() {
        return "/admin/time";
    }

    @GetMapping("/theme")
    public String getThemePage() {
        return "/admin/theme";
    }
}
