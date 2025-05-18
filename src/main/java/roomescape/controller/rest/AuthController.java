package roomescape.controller.rest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.LoginMember;
import roomescape.dto.request.LoginRequest;
import roomescape.service.LoginService;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final LoginService loginService;

    public AuthController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LoginRequest request,
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
