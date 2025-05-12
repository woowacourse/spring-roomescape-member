package roomescape.presentation.controller.user.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public final class UserPageController {

    @GetMapping
    public String bestTheme() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }
}
