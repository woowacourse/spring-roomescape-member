package roomescape.controller.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminReservationTimePageController {

    @GetMapping("/admin/time")
    public String getPage() {
        return "/admin/time";
    }
}
