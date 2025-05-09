package roomescape.auth.login.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginPageController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("admin/login")
    public String adminLoginPage() {
        return "admin/login";
    }
}
