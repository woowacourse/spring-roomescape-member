package roomescape.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {

    @GetMapping("login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/reservation")
    public String mainPage() {
        return "reservation";
    }
}

