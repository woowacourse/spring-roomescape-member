package roomescape;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberViewController {

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }

    /**
     * TODO
     * Controller 분리 고민하기
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
