package roomescape.user.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/reservation")
    public String reservation(
    ) {
        return "/reservation";
    }
}
