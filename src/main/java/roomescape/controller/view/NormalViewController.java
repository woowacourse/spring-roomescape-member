package roomescape.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NormalViewController {

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }
}
