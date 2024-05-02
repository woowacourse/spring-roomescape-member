package roomescape.theme.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThemePageController {

    @GetMapping("/admin/theme")
    public String adminThemePage() {
        return "admin/theme";
    }

    @GetMapping("/")
    public String popularPage() {
        return "index";
    }
}
