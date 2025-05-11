package roomescape.presentation.controller.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.auth.AuthService;
import roomescape.application.auth.dto.MemberAuthRequest;
import roomescape.application.auth.dto.MemberAuthResponse;
import roomescape.application.auth.dto.TokenRequest;
import roomescape.application.auth.dto.TokenResponse;
import roomescape.application.dto.MemberDto;
import roomescape.infrastructure.AuthenticationPrincipal;

@RestController
@RequestMapping("/login")
public class TokenLoginController {

    private final AuthService authService;

    public TokenLoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody TokenRequest tokenRequest, HttpServletResponse response) {
        TokenResponse tokenResponse = authService.createToken(tokenRequest);
        Cookie cookie = authService.createCookie(tokenResponse.accessToken());

        response.addCookie(cookie);
        response.setHeader("Set-Cookie", cookie.getAttribute("token"));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberAuthResponse> checkLogin(@AuthenticationPrincipal MemberAuthRequest memberAuthRequest) {
        MemberDto memberDto = authService.getMemberById(memberAuthRequest.id());
        MemberAuthResponse memberAuthResponse = MemberAuthResponse.from(memberDto);
        return ResponseEntity.ok(memberAuthResponse);
    }
}
