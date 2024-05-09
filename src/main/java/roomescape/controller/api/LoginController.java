package roomescape.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.MemberResponse;
import roomescape.dto.TokenRequest;
import roomescape.dto.TokenResponse;
import roomescape.exception.AuthenticationException;
import roomescape.service.AuthService;

@RestController
public class LoginController {

    private static final String TOKEN_FIELD = "token";

    private final AuthService authService;

    public LoginController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public void login(@RequestBody final TokenRequest tokenRequest, final HttpServletResponse response) {
        final TokenResponse tokenResponse = authService.createToken(tokenRequest);
        final Cookie cookie = new Cookie(TOKEN_FIELD, tokenResponse.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(final HttpServletRequest request) {
        final String token = extractTokenFromRequestCookie(request);
        final MemberResponse memberResponse = authService.findMemberByToken(token);
        return ResponseEntity.ok(memberResponse);
    }

    private String extractTokenFromRequestCookie(final HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            if (TOKEN_FIELD.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new AuthenticationException("토큰 정보가 존재하지 않습니다.");
    }
}
