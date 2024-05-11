package roomescape.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Member;
import roomescape.dto.LoginRequest;
import roomescape.dto.TokenResponse;
import roomescape.service.AuthService;
import roomescape.utils.CookieUtils;

@RestController
public class LoginController {
    private final AuthService authService;

    public LoginController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        final TokenResponse tokenResponse = authService.createToken(loginRequest);
        CookieUtils.addTokenCookie(response, tokenResponse.getAccessToken());

        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/login/check")
    public ResponseEntity<Member> checkLogin(HttpServletRequest request) {
        final String token = CookieUtils.extractTokenFrom(request);
        final Member member = authService.findMemberByToken(token);

        return ResponseEntity.ok(member);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        CookieUtils.expireTokenCookie(response);
        return ResponseEntity.ok().build();
    }
}
