package roomescape.controller.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/theme")
public class AdminThemePageController {

    @GetMapping
    public String getThemePage() {
        return "/admin/theme";
    }
}
