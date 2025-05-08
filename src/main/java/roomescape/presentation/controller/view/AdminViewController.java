package roomescape.presentation.controller.view;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @GetMapping
    public String adminHome(Model model) {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String reservationPage(Model model) {
        return "admin/reservation-new";
    }

    @GetMapping("/time")
    public String timePage(Model model) {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String themePage(Model model) {
        return "admin/theme";
    }
}
