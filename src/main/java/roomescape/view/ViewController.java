package roomescape.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/reservations")
    public String userReservationPage() {

        return "user/reservations";
    }

    @GetMapping("/themes")
    public String themeReservationPage() {
        return "user/themes";
    }

    @GetMapping("/my-reservations")
    public String myReservationPage() {
        return "user/my-reservations";
    }

    @GetMapping("/admin")
    public String adminReservationPage() {
        return "admin/management";
    }
}
