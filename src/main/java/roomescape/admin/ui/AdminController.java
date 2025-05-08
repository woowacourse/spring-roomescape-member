package roomescape.admin.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.auth.domain.RequiresRole;
import roomescape.member.domain.UserRole;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    @RequiresRole(userRoles = UserRole.ADMIN)
    public String home() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    @RequiresRole(userRoles = UserRole.ADMIN)
    public String reservation() {
        return "admin/reservation-new";
    }

    @GetMapping("/time")
    @RequiresRole(userRoles = UserRole.ADMIN)
    public String time() {
        return "admin/time";
    }

    @GetMapping("/theme")
    @RequiresRole(userRoles = UserRole.ADMIN)
    public String theme() {
        return "admin/theme";
    }
}
