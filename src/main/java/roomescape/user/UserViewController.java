package roomescape.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }

    @GetMapping
    public String main() {
        return "index";
    }
}
