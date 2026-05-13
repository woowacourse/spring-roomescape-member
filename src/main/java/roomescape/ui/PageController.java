package roomescape.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String userPage() {
        return "user";
    }

    @GetMapping("/reservation-lookup")
    public String reservationLookupPage() {
        return "reservation-lookup";
    }

    @GetMapping("/admin-page")
    public String adminPage() {
        return "admin";
    }
}
