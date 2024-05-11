package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.helper.CookieExtractor;
import roomescape.controller.helper.LoginMember;
import roomescape.domain.Member;
import roomescape.service.AuthService;
import roomescape.service.dto.LoginCheckResponse;
import roomescape.service.dto.LoginRequest;

@RestController
public class AuthController {
    private final CookieExtractor cookieExtractor;
    private final AuthService authService;

    public AuthController(CookieExtractor cookieExtractor, AuthService authService) {
        this.cookieExtractor = cookieExtractor;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String token = authService.login(request);
        Cookie cookie = cookieExtractor.createCookie(token);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(@LoginMember Member member) {
        LoginCheckResponse response = authService.loginCheck(member);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@LoginMember Member member) {
        authService.loginCheck(member); // TODO: 해당 검증 로직의 필요 여부 판단해보기(댜른 곳에서?)
        return ResponseEntity.ok().build();
    }
}
