package roomescape.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {
    @GetMapping("/reservation")
    public String reservationNew() {
        return "/reservation";
    }

    @GetMapping("/")
    public String home() {
        return "/index";
    }
}
