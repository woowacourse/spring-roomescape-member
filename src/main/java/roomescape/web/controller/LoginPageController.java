package roomescape.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class LoginPageController {

    @GetMapping("/login")
    public String getLoginPage() {
        return "/login";
    }
}
