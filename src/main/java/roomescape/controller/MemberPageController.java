package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberPageController {

    @GetMapping
    public String showMemberIndexPage() {
        return "index";
    }

    @GetMapping("/reservation")
    public String showMemberReservationPage() {
        return "reservation";
    }


}
