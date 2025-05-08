package roomescape.presentation.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }
}
