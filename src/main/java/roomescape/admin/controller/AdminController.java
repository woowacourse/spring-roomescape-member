package roomescape.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String admin(
    ) {
        return "/admin/index.html";
    }

    @GetMapping("/reservation")
    public String reservation(
    ) {
        return "/admin/reservation-legacy.html";
    }
}
