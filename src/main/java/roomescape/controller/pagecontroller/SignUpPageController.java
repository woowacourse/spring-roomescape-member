package roomescape.controller.pagecontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignUpPageController {
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
}
