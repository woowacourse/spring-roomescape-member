package roomescape.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/")
    public String getPopularThemePage() {
        return "index";
    }

    @GetMapping("/reservation")
    public String getUserReservationPage() {
        return "reservation";
    }
}
