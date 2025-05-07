package roomescape.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberPageController {

    @GetMapping("/signup")
    public String getSignUpPage() {
        return "/signup";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "/login";
    }
}
