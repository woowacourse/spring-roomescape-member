package roomescape.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthViewController {
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/signup")
    public String showSignup() {
        return "signup";
    }
}
