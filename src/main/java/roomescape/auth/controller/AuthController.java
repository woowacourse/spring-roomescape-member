package roomescape.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.TokenResponse;
import roomescape.auth.infrastructure.TokenCookieManager;
import roomescape.auth.resolver.LoginMember;
import roomescape.auth.service.AuthService;
import roomescape.member.dto.MemberResponse;

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
    public ResponseEntity<MemberResponse> findInfo(@LoginMember MemberResponse member) {
        return ResponseEntity.ok().body(member);
    }
}
