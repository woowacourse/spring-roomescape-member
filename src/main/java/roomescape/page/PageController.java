package roomescape.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String getPopularThemePage() {
        return "index";
    }

    @GetMapping("/reservation")
    public String getUserReservationPage() {
        return "reservation";
    }

    @GetMapping("/admin")
    public String getMainPage() {
        return "/admin/index";
    }

    @GetMapping("/admin/reservation")
    public String getReservationPage() {
        return "/admin/reservation-new";
    }

    @GetMapping("/admin/time")
    public String getTimePage() {
        return "/admin/time";
    }

    @GetMapping("/admin/theme")
    public String getThemePage() {
        return "/admin/theme";
    }
}
