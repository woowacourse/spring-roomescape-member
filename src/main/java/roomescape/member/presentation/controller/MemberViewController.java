package roomescape.member.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberViewController {
    @GetMapping("/reservation")
    public String reservation(
    ) {
        return "/reservation";
    }
}
