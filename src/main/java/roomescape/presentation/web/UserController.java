package roomescape.presentation.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reservation")
public class UserController {

    @GetMapping
    public String getReservation() {
        return "reservation";
    }
}
