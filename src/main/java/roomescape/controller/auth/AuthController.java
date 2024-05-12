package roomescape.controller.auth;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.argumentresolver.LoginMember;
import roomescape.domain.member.MemberInfo;
import roomescape.dto.auth.LoginRequest;
import roomescape.dto.auth.TokenResponse;
import roomescape.infrastructure.TokenCookieManager;
import roomescape.service.auth.AuthService;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        TokenResponse tokenResponse = authService.createToken(loginRequest);
        TokenCookieManager.setTokenCookie(tokenResponse.accessToken(), response);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberInfo> findInfo(@LoginMember MemberInfo member) {
        return ResponseEntity.ok().body(member);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        TokenCookieManager.expireToken(response);

        return ResponseEntity.ok().build();
    }
}
