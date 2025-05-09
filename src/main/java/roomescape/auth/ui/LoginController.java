package roomescape.auth.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.auth.domain.AuthRole;
import roomescape.auth.domain.RequiresRole;

@Controller
public class LoginController {

    @GetMapping("/login")
    @RequiresRole(authRoles = {AuthRole.ADMIN, AuthRole.MEMBER, AuthRole.GUEST})
    public String login() {
        return "login";
    }
}
