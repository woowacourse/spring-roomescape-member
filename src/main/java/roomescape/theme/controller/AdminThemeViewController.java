package roomescape.theme.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/theme")
public class AdminThemeViewController {

    @GetMapping
    public String getTheme() {
        return "admin/theme";
    }
}
