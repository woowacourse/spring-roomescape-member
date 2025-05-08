package roomescape.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/reservation")
    public String reservation(
    ) {
        return "/reservation";
    }

    @GetMapping("/login")
    public String login(
    ) {
        return "/login";
    }

    @GetMapping("/signup")
    public String signUp(
    ) {
        return "/signup";
    }

    @GetMapping("/")
    public String index(
    ) {
        return "/index";
    }


}
