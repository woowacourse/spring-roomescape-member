package roomescape.theme.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminThemeViewController {

    @GetMapping("/theme")
    public String theme() {
        return "admin/theme";
    }

}
