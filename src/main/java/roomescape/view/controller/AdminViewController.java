package roomescape.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.global.annotation.Auth;
import roomescape.member.role.MemberRole;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @Auth(role = MemberRole.ADMIN)
    @GetMapping
    public String mainPage() {
        return "admin/index";
    }

    @Auth(role = MemberRole.ADMIN)
    @GetMapping("/reservation")
    public String reservationPage() {
        return "admin/reservation-new";
    }

    @Auth(role = MemberRole.ADMIN)
    @GetMapping({"/time"})
    public String timePage() {
        return "admin/time";
    }

    @Auth(role = MemberRole.ADMIN)
    @GetMapping("/theme")
    public String themePage() {
        return "admin/theme";
    }
}
