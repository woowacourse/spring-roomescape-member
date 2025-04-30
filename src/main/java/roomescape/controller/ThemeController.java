package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThemeController {

    @GetMapping("/admin/theme")
    public String displayAdminTheme() {
        return "/admin/theme";
    }
}
