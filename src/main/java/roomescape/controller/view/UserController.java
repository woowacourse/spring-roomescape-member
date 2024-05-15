package roomescape.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class UserController {
    @GetMapping("reservation")
    public String getReservationPage() {
        return "/reservation";
    }

    @GetMapping
    public String getHomePage() {
        return "/index";
    }
}
