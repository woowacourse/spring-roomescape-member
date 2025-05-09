package roomescape.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.global.auth.Auth;
import roomescape.member.domain.Role;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    @Auth(Role.ADMIN)
    public String admin(
    ) {
        return "/admin/index";
    }

    @GetMapping("/reservation")
    @Auth(Role.ADMIN)
    public String reservation(
    ) {
        return "/admin/reservation-new";
    }

    @GetMapping("/time")
    @Auth(Role.ADMIN)
    public String time(
    ) {
        return "/admin/time";
    }

    @GetMapping("/theme")
    @Auth(Role.ADMIN)
    public String theme(
    ) {
        return "/admin/theme";
    }
}
