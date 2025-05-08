package roomescape.member.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.auth.domain.RequiresRole;
import roomescape.member.domain.UserRole;

@Controller
public class MemberController {

    @GetMapping
    @RequiresRole(userRoles = {UserRole.ADMIN, UserRole.MEMBER, UserRole.GUEST})
    public String home() {
        return "index";
    }

    @GetMapping("/reservation")
    @RequiresRole(userRoles = {UserRole.ADMIN, UserRole.MEMBER, UserRole.GUEST})
    public String reservation() {
        return "reservation";
    }

    @GetMapping("/signup")
    @RequiresRole(userRoles = {UserRole.ADMIN, UserRole.MEMBER, UserRole.GUEST})
    public String signUp() {
        return "signup";
    }
}
