package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.LoginRequest;
import roomescape.dto.MemberResponse;
import roomescape.exception.RoomEscapeException;
import roomescape.service.LoginService;

@RestController
public class MemberLoginController {

    private static final String TOKEN = "token";

    private final LoginService loginService;

    public MemberLoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String accessToken = loginService.login(request);

        Cookie cookie = new Cookie(TOKEN, accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        String token = extractTokenFromCookie(request.getCookies());
        MemberResponse response = loginService.checkMember(token);

        return ResponseEntity.ok(response);
    }

    public String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (TOKEN.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new RoomEscapeException("[ERROR] 토큰이 존재하지 않습니다.");
    }
}
