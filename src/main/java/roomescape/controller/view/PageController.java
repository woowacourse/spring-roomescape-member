package roomescape.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {


    @GetMapping("/login")
    public String loginPage() {
        return "/login";
    }

    @GetMapping("/reservation")
    public String reservationPage() {
        return "/reservation";
    }
}
