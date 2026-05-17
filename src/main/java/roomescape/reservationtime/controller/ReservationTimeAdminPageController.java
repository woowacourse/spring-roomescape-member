package roomescape.reservationtime.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pages/admin/themes/{themeId}/times")
public class ReservationTimeAdminPageController {

    @GetMapping
    public String read() {
        return "reservationtime/list";
    }

}
