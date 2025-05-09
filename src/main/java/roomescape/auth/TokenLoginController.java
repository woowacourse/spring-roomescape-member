package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.dto.MemberDto;
import roomescape.auth.dto.MemberAuthRequest;
import roomescape.auth.dto.MemberAuthResponse;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.dto.TokenResponse;
import roomescape.infrastructure.AuthenticationPrincipal;

@RestController
public class TokenLoginController {

    private final AuthService authService;

    public TokenLoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody TokenRequest tokenRequest, HttpServletResponse response) {
        TokenResponse tokenResponse = authService.createToken(tokenRequest);
        Cookie cookie = authService.createCookie(tokenResponse.accessToken());

        response.addCookie(cookie);
        response.setHeader("Set-Cookie", cookie.getAttribute("token"));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberAuthResponse> checkLogin(@AuthenticationPrincipal MemberAuthRequest memberAuthRequest) {
        MemberDto memberDto = authService.getMemberById(memberAuthRequest.id());
        MemberAuthResponse memberAuthResponse = MemberAuthResponse.from(memberDto);
        return ResponseEntity.ok(memberAuthResponse);
    }
}
