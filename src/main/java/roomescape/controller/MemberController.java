package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.MemberLoginResponse;
import roomescape.dto.TokenRequest;
import roomescape.dto.TokenResponse;
import roomescape.infrastructure.AuthorizationExtractor;
import roomescape.service.MemberService;

@RestController
public class MemberController {

    private final MemberService memberService;
    private final AuthorizationExtractor authorizationExtractor;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
        this.authorizationExtractor = new AuthorizationExtractor();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody final TokenRequest request, final HttpServletResponse response) {
        final TokenResponse tokenResponse = memberService.createToken(request);
        final Cookie cookie = new Cookie("token", tokenResponse.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberLoginResponse> findMemberInfo(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        final String accessToken = authorizationExtractor.extractTokenFromCookie(cookies);
        final MemberLoginResponse memberLoginResponse = memberService.findMemberByToken(accessToken);
        return ResponseEntity.ok(memberLoginResponse);
    }
}
