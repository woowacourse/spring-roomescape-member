package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.helper.LoginMember;
import roomescape.controller.helper.RoleAllowed;
import roomescape.domain.Member;
import roomescape.service.AuthService;
import roomescape.service.dto.LoginCheckResponse;
import roomescape.service.dto.LoginRequest;
import roomescape.service.dto.SignupRequest;
import roomescape.service.dto.SignupResponse;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        Cookie cookie = authService.login(request);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @RoleAllowed
    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(@LoginMember Member member) {
        LoginCheckResponse response = authService.loginCheck(member);
        return ResponseEntity.ok().body(response);
    }

    @RoleAllowed
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = authService.logout();
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request);
        return ResponseEntity.created(URI.create("/members/" + response.getId())).body(response);
    }
}
