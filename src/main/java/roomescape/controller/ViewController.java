package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.domain.member.Member;

@Controller
public class ViewController {

    @GetMapping("/")
    public String popularThemePage() {
        return "index";
    }

    @GetMapping("/reservation")
    public String reservationPage(Member member) {
        if (member == null) {
            return loginPage(null);
        }
        return "reservation";
    }

    @GetMapping("/login")
    public String loginPage(Member member) {
        if (member == null) {
            return "login";
        }
        return popularThemePage();
    }

    @GetMapping("/signup")
    public String signupPage(Member member) {
        return "signup";
    }
}
