package roomescape.controller.api;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.AuthService;
import roomescape.domain.member.Member;
import roomescape.dto.auth.TokenRequest;
import roomescape.dto.member.MemberNameResponse;

@RestController
@RequestMapping("/login")
public class TokenLoginController {
    private static final String TOKEN_NAME = "token";

    private final AuthService authService;

    public TokenLoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> tokenLogin(@Valid @RequestBody TokenRequest tokenRequest) {
        ResponseCookie tokenResponse = authService.createCookie(tokenRequest);

        return ResponseEntity.ok().header(SET_COOKIE, tokenResponse.toString()).build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberNameResponse> findMyInfo(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String tokenValue = extractTokenFromCookie(List.of(cookies));

        Member member = authService.findMemberByToken(tokenValue);

        return ResponseEntity.ok(new MemberNameResponse(member.getName().getValue()));
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
