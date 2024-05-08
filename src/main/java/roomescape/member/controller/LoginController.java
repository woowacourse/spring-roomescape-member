package roomescape.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.service.AuthService;

import java.util.List;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> tokenLogin(HttpServletResponse response, @RequestBody MemberRequest memberRequest) {
        String accessToken = authService.createToken(memberRequest);

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        String token = extractTokenFromCookie(List.of(request.getCookies()));
        MemberResponse memberResponse = authService.findMemberByToken(token);

        return ResponseEntity.ok(memberResponse);
    }

    private String extractTokenFromCookie(List<Cookie> cookies) {
        return cookies.stream()
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse("");
    }
}
