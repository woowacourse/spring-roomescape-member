package roomescape.presentation.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }
}
