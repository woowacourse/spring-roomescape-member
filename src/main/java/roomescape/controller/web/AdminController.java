package roomescape.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// TODO API 클래스 한번 쭉 정리하기
@Controller
public class AdminController {

    @GetMapping("/")
    public String getHomePage() {
        return "/index";
    }

    @GetMapping("/admin")
    public String getAdminPage() {
        return "/admin/index";
    }

    @GetMapping("/admin/reservation")
    public String getAdminReservationPage() {
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

    @GetMapping("/reservation")
    public String getReservationPage() {
        return "/reservation";
    }
}
