package roomescape.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.dto.request.LoginRequest;

@Controller
@RequestMapping("/login")
public class LoginPageController {

    @GetMapping
    public String loginPage() {
        return "login";
    }

    @PostMapping
    public void login(@RequestBody LoginRequest loginRequest) {
        System.out.println("loginRequest = " + loginRequest);
    }
}
