package roomescape.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.auth.RoleRequired;
import roomescape.member.entity.Role;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @GetMapping
    @RoleRequired(Role.ADMIN)
    public String getMainPage() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    @RoleRequired(Role.ADMIN)
    public String getReservationPage() {
        return "admin/reservation-new";
    }

    @GetMapping("/time")
    @RoleRequired(Role.ADMIN)
    public String getTimePage() {
        return "admin/time";
    }

    @GetMapping("/theme")
    @RoleRequired(Role.ADMIN)
    public String getThemePage() {
        return "admin/theme";
    }
}
