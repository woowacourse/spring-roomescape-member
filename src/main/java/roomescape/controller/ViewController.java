package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
//    @GetMapping()
//    String welcome() {
//        return "welcomePage";
//    }

    @GetMapping("/admin")
    String admin() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    String adminReservation() {
        return "admin/reservation-new";
    }

    @GetMapping("/admin/time")
    String adminTime() {
        return "admin/time";
    }

    @GetMapping("/admin/theme")
    String adminTheme() {
        return "admin/theme";
    }
}
