package roomescape.presentation.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public final class LoginPageController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
