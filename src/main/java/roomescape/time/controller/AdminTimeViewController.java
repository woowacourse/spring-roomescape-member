package roomescape.time.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/time")
public class AdminTimeViewController {

    @GetMapping
    public String getTime() {
        return "admin/time";
    }
}
