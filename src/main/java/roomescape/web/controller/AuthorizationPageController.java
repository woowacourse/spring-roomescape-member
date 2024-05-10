package roomescape.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthorizationPageController {

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }
}
