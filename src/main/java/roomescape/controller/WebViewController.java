package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class WebViewController {

    @GetMapping({"", "/"})
    public String welcome() {
        return "index";
    }

    @GetMapping("/admin")
    public String adminHome() {
        return "admin/dashboard";
    }

    @GetMapping("/admin/reservations")
    public String adminReservations() {
        return "admin/reservations";
    }

    @GetMapping("/admin/times")
    public String adminTimes() {
        return "admin/times";
    }

    @GetMapping("/admin/themes")
    public String adminThemes() {
        return "admin/themes";
    }

    @GetMapping("/user")
    public String userHome() {
        return "user/home";
    }

    @GetMapping("/user/reserve")
    public String userReserve() {
        return "user/reserve";
    }

    @GetMapping("/user/reservations")
    public String userReservations() {
        return "user/reservations";
    }

    @GetMapping("/user/popular")
    public String userPopular() {
        return "user/popular";
    }
}
