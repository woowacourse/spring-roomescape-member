package roomescape.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/signup")
@Controller
public class SignUpPageController {

    @GetMapping
    public String getSignupPage() {
        return "signup";
    }
}
