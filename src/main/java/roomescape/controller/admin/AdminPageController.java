package roomescape.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    @GetMapping
    public String displayMain() {
        return "/admin/index";
    }

    @GetMapping("/reservation")
    public String displayAdminReservation() {
        return "/admin/reservation-new";
    }

    @GetMapping("/time")
    public String displayAdminTime() {
        return "/admin/time";
    }

    @GetMapping("/theme")
    public String displayAdminTheme() {
        return "/admin/theme";
    }
}
