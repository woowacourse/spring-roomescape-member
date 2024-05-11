package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.controller.security.Permission;
import roomescape.domain.member.Role;

@Controller
@Permission(role = Role.NONE)
public class MemberViewController {
    @GetMapping("/reservation")
    public String reservationUserPage() {
        return "reservation";
    }

    @GetMapping
    public String mainUserPage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
