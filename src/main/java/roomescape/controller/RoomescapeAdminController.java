package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class RoomescapeAdminController {

    @GetMapping
    public String getAdminPage() {
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
