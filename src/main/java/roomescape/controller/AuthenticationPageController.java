package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationPageController {
    @GetMapping("/signup")
    public String postSignUp() {
        return "/signup";
    }
}
