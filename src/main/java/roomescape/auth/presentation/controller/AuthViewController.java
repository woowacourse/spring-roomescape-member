package roomescape.auth.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthViewController {
    @GetMapping("/login")
    public String login(
    ) {
        return "/login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "/signup";
    }
}
