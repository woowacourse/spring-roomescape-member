package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.config.CookieExtractor;
import roomescape.domain.LoginMember;
import roomescape.dto.LoginRequest;
import roomescape.dto.LoginResponse;
import roomescape.service.LoginService;

@Controller
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String loginView() {
        return "/login";
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String loginToken = loginService.getLoginToken(loginRequest);
        Cookie cookie = new Cookie("token", loginToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie token = CookieExtractor.getTokenCookie(request);
        token.setMaxAge(0);
        response.addCookie(token);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginResponse> loginCheck(@LoginMemberParameter LoginMember loginMember) {
        return ResponseEntity.ok(new LoginResponse(loginMember.getName()));
    }
}
