package roomescape.presentation.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.auth.AuthRequired;
import roomescape.business.model.vo.UserRole;

@Controller
public class AdminPageController {

    @GetMapping("/admin")
    @AuthRequired(UserRole.ADMIN)
    public String getMainPage() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    @AuthRequired(UserRole.ADMIN)
    public String getReservationPage() {
        return "admin/reservation";
    }

    @GetMapping("/admin/time")
    @AuthRequired(UserRole.ADMIN)
    public String getReservationTimePage() {
        return "admin/time";
    }

    @GetMapping("/admin/theme")
    @AuthRequired(UserRole.ADMIN)
    public String getThemePage() {
        return "admin/theme";
    }
}
