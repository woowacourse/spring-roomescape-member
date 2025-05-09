package roomescape.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.domain.LoginMember;

@Controller
public class ReservationPageController {

    @GetMapping("/reservation")
    public String getReservationPage(LoginMember member) {
        return "reservation";
    }
}
