package roomescape.auth.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.service.AuthService;
import roomescape.auth.web.controller.request.LoginRequest;
import roomescape.auth.web.controller.response.MemberNameResponse;
import roomescape.auth.web.resolver.Authenticated;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        String accessToken = authService.login(request);

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie expiredCookie = new Cookie("token", null);
        expiredCookie.setPath("/");
        expiredCookie.setMaxAge(0);
        expiredCookie.setHttpOnly(true);
        response.addCookie(expiredCookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberNameResponse> check(@Authenticated Long memberId) {
        MemberNameResponse response = authService.check(memberId);

        return ResponseEntity.ok(response);
    }
}
