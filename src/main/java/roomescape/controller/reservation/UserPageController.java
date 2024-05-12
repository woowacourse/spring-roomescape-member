package roomescape.controller.reservation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }
}
