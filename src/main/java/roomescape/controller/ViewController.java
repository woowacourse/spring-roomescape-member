package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping(path = {"/", "/admin"})
    public String wellComePage() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    public String reservationPage() {
        return "admin/reservation";
    }

    @GetMapping("admin/time")
    public String reservationTimePage() {
        return "admin/time";
    }
}
