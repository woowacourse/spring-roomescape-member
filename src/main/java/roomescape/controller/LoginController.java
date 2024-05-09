package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.Member;
import roomescape.dto.member.MemberResponse;
import roomescape.dto.member.UserLoginRequest;
import roomescape.service.AuthService;
import roomescape.service.exception.AuthorizationException;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody @Valid UserLoginRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("token", authService.createToken(request));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> check(HttpServletRequest request, HttpServletResponse response) {
        if (request.getCookies() == null) {
            throw new AuthorizationException("사용자 정보를 조회할 수 없습니다.");
        }
        String token = extractTokenFromCookie(request.getCookies());
        Member member = authService.findMemberByToken(token);
        return ResponseEntity.ok().body(MemberResponse.from(member.getName()));
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
