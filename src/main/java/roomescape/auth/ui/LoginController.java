package roomescape.auth.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.auth.domain.RequiresRole;
import roomescape.member.domain.UserRole;

@Controller
public class LoginController {

    @GetMapping("/login")
    @RequiresRole(userRoles = {UserRole.ADMIN, UserRole.MEMBER, UserRole.GUEST})
    public String login() {
        return "login";
    }
}
