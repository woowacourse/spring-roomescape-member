package roomescape.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/login")
@Controller
public class LoginPageController {

    @GetMapping
    public String getLoginPage() {
        return "login";
    }
}
