package roomescape.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.AuthService;
import roomescape.application.dto.MemberResponse;
import roomescape.dto.auth.TokenRequest;
import roomescape.dto.auth.TokenResponse;

@RestController
public class TokenLoginController {
    private static final String TOKEN_NAME = "token";

    private final AuthService authService;

    public TokenLoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> tokenLogin(@Valid @RequestBody TokenRequest tokenRequest,
                                           HttpServletResponse response) {
        TokenResponse tokenResponse = authService.createToken(tokenRequest);

        Cookie cookie = new Cookie(TOKEN_NAME, tokenResponse.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> findMyInfo(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String tokenValue = extractTokenFromCookie(List.of(cookies));

        MemberResponse response = authService.findMemberByToken(tokenValue);

        return ResponseEntity.ok(response);
    }

    private String extractTokenFromCookie(List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN_NAME)) {
                return cookie.getValue();
            }
        }

        return "";
    }
}
