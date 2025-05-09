package roomescape.member.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.auth.domain.AuthRole;
import roomescape.auth.domain.RequiresRole;

@Controller
public class MemberController {

    @GetMapping
    @RequiresRole(authRoles = {AuthRole.ADMIN, AuthRole.MEMBER, AuthRole.GUEST})
    public String home() {
        return "index";
    }

    @GetMapping("/reservation")
    @RequiresRole(authRoles = {AuthRole.ADMIN, AuthRole.MEMBER, AuthRole.GUEST})
    public String reservation() {
        return "reservation";
    }

    @GetMapping("/signup")
    @RequiresRole(authRoles = {AuthRole.ADMIN, AuthRole.MEMBER, AuthRole.GUEST})
    public String signUp() {
        return "signup";
    }
}
