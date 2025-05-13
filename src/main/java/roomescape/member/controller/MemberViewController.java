package roomescape.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberViewController {

    @GetMapping("/")
    public String getMainPage() {
        return "index";
    }

    @GetMapping("/signup")
    public String getSignUpPage() {
        return "signup";
    }
}
