package roomescape.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.dto.LoginRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.service.LoginService;
import roomescape.global.auth.AuthUser;

@RestController
public class MemberLoginRestController {

    private static final String TOKEN = "token";

    private final LoginService loginService;

    public MemberLoginRestController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String accessToken = loginService.login(request);

        Cookie cookie = new Cookie(TOKEN, accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(AuthUser authUser) {
        return ResponseEntity.ok(new MemberResponse(authUser.id(), authUser.name()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (TOKEN.equals(cookie.getName())) {
                Cookie emptyCookie = new Cookie("token", "");
                emptyCookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }

        return ResponseEntity.ok().build();
    }
}
