package roomescape.controller.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.domain.Email;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.request.TokenRequest;
import roomescape.service.AuthService;

@Controller
@RequestMapping("/login")
public class LoginPageController {

    private final AuthService authService;

    public LoginPageController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String loginPage() {
        return "login";
    }

    @PostMapping
    public void login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        TokenRequest tokenRequest = new TokenRequest(new Email(loginRequest.email()));
        Cookie cookie = new Cookie("token", authService.createToken(tokenRequest).token());
        response.addCookie(cookie);
    }
}
