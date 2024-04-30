package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoomEscapeController {

    @GetMapping("/admin")
    public String getAdminPage() {
        return "/admin/index";
    }

    @GetMapping
    public String getAdminPageByRedirect() {
        return "redirect:/admin";
    }

    @GetMapping("/admin/reservation")
    public String getReservationPage() {
        return "/admin/reservation";
    }

    @GetMapping("/admin/time")
    public String getTimePage() {
        return "/admin/time";
    }
}
