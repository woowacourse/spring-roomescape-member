package roomescape.controller.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.auth.MemberLoginResponse;
import roomescape.dto.auth.TokenRequest;
import roomescape.dto.auth.TokenResponse;
import roomescape.service.MemberService;

@RestController
public class AuthController {

    private static final String TOKEN_KEY = "token";

    private final MemberService memberService;
    private final AuthorizationExtractor authorizationExtractor;

    public AuthController(final MemberService memberService) {
        this.memberService = memberService;
        this.authorizationExtractor = new AuthorizationExtractor();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody final TokenRequest request, final HttpServletResponse response) {
        final TokenResponse tokenResponse = memberService.createToken(request);
        final Cookie cookie = new Cookie(TOKEN_KEY, tokenResponse.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberLoginResponse> findMemberInfo(final HttpServletRequest request) {
        final String accessToken = authorizationExtractor.extractToken(request);

        final MemberLoginResponse memberLoginResponse = memberService.findMemberByToken(accessToken);
        return ResponseEntity.ok(memberLoginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(final HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN_KEY, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
