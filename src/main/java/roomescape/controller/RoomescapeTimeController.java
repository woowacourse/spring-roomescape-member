package roomescape.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoomescapeTimeController {

    @GetMapping("/admin/time")
    public String getTimePage() {
        return "admin/time";
    }
}
