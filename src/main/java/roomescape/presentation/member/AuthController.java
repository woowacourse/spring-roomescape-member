package roomescape.presentation.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.member.MemberService;
import roomescape.application.member.TokenManager;
import roomescape.application.member.dto.request.MemberLoginRequest;
import roomescape.application.member.dto.response.MemberResponse;
import roomescape.application.member.dto.response.TokenResponse;

@RestController
public class AuthController {
    private static final String TOKEN_COOKIE_NAME = "token";

    private final MemberService memberService;
    private final TokenManager tokenManager;

    public AuthController(MemberService memberService, TokenManager tokenManager) {
        this.memberService = memberService;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid MemberLoginRequest request, HttpServletResponse response) {
        TokenResponse login = memberService.login(request);
        Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, login.token());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public MemberResponse check(HttpServletRequest request) {
        String token = extractTokenFromCookies(request.getCookies());
        return tokenManager.parseToken(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.noContent().build();
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN_COOKIE_NAME)) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
