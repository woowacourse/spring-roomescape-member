package roomescape.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberPageController {

    @GetMapping("/member/reservation")
    public String reservation() {
        return "reservation";
    }

    @GetMapping("/member/login")
    public String login() {
        return "login";
    }
}
