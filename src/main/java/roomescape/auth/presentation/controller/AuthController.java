package roomescape.auth.presentation.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.application.service.AuthService;
import roomescape.auth.presentation.CookieManager;
import roomescape.auth.presentation.dto.LoginRequest;
import roomescape.auth.presentation.dto.TokenResponse;
import roomescape.member.domain.Member;
import roomescape.member.presentation.dto.MemberNameResponse;

@RestController
public class AuthController {

    private final AuthService authService;
    private final CookieManager cookieManager;

    public AuthController(AuthService authService) {
        this.authService = authService;
        this.cookieManager = new CookieManager();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        TokenResponse token = authService.login(loginRequest);
        cookieManager.setTokenCookie(token.accessToken(), response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberNameResponse> checkLogin(Member member) {
        return ResponseEntity.ok().body(new MemberNameResponse(member.getName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        cookieManager.deleteTokenCookie(response);
        return ResponseEntity.ok().build();
    }
}
