package roomescape.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.global.auth.AuthUser;
import roomescape.global.auth.CookieManager;
import roomescape.member.dto.LoginRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.service.LoginService;

import java.util.List;

@RestController
public class LoginRestController {

    private final CookieManager cookieManager = new CookieManager();
    private final LoginService loginService;

    public LoginRestController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String token = loginService.login(request);

        Cookie cookie = cookieManager.createCookie(token);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(AuthUser authUser) {
        return ResponseEntity.ok(new MemberResponse(authUser.id(), authUser.name()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        List<Cookie> cookies = cookieManager.extractCookies(request);

        Cookie cookie = cookieManager.expireAuthCookie(cookies);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }
}
