package roomescape.controller.page;

import org.springframework.web.bind.annotation.GetMapping;

public class UserPageController {
    @GetMapping("/")
    public String getHomePage() {
        return "/index";
    }

    @GetMapping("/reservation")
    public String getReservationPage() {
        return "/reservation";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "/login";
    }
}
