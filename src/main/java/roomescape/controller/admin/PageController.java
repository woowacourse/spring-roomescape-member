package roomescape.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller(value = "AdminPageController")
public class PageController {

    @GetMapping("/admin")
    public String getMainPage() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    public String getReservationPage() {
        return "admin/reservation-new";
    }

    @GetMapping("/admin/time")
    public String getTimePage() {
        return "admin/time";
    }

    @GetMapping("/admin/theme")
    public String getThemePage() {
        return "admin/theme";
    }
}
