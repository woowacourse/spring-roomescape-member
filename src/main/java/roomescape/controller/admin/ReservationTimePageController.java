package roomescape.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservationTimePageController {

    @GetMapping("/admin/time")
    public String getPage() {
        return "/admin/time";
    }
}
