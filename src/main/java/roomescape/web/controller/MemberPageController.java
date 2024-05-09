package roomescape.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class MemberPageController {

    @GetMapping("/reservation")
    public String getReservationPage() {
        return "/reservation";
    }
}
