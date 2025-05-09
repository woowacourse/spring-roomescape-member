package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.dto.LoginRequest;
import roomescape.dto.request.LoginMember;
import roomescape.service.LoginService;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping
    public String home() {
        return "login";
    }

    @PostMapping
    public ResponseEntity<HttpServletResponse> login(@RequestBody LoginRequest request,
        HttpServletResponse response) {
        String accessToken = loginService.login(request);
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<LoginMember> checkLogin(LoginMember loginMember) {
        return ResponseEntity.ok().body(loginMember);
    }
}
