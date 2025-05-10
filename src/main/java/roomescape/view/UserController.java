package roomescape.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.global.auth.Auth;
import roomescape.member.domain.Role;

@Controller
public class UserController {

    @GetMapping("/reservation")
    @Auth(Role.USER)
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
