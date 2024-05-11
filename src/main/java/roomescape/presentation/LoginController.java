package roomescape.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.AuthService;
import roomescape.application.dto.MemberResponse;
import roomescape.application.dto.TokenRequest;
import roomescape.application.dto.TokenResponse;
import roomescape.exception.RoomescapeErrorCode;
import roomescape.exception.RoomescapeException;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody TokenRequest request, HttpServletResponse response) {
        TokenResponse token = authService.createToken(request);
        Cookie cookie = new Cookie("token", token.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> check(HttpServletRequest request) {
        String token = extractTokenFromCookie(request.getCookies());
        if (token == null || token.isBlank()) {
            throw new RoomescapeException(RoomescapeErrorCode.UNAUTHORIZED);
        }
        MemberResponse memberResponse = authService.findMemberByToken(token);
        return ResponseEntity.ok(memberResponse);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            return "";
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findAny()
                .map(Cookie::getValue)
                .orElse("");
    }
}
