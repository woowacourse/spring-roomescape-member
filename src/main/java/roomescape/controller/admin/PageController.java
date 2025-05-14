package roomescape.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller(value = "AdminPageController")
@RequestMapping("/admin")
public class PageController {

    @GetMapping
    public String getMainPage() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String getReservationPage() {
        return "admin/reservation-new";
    }

    @GetMapping("/time")
    public String getTimePage() {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String getThemePage() {
        return "admin/theme";
    }
}
