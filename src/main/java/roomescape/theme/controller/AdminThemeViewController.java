package roomescape.theme.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/theme")
public class AdminThemeViewController {

    public String getThemes() {
        return "admin/theme";
    }
}
