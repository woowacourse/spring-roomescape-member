package roomescape.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.admin.domain.Admin;
import roomescape.auth.infrastructure.argument.AuthorizedAdmin;


@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @GetMapping
    public String home(@AuthorizedAdmin Admin authorizedAdmin) {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String reservation(@AuthorizedAdmin Admin authorizedAdmin) {
        return "admin/reservation-new";
    }

    @GetMapping("/time")
    public String time(@AuthorizedAdmin Admin authorizedAdmin) {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String theme(@AuthorizedAdmin Admin authorizedAdmin) {
        return "admin/theme";
    }

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }
}
