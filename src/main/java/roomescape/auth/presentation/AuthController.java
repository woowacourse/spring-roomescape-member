package roomescape.auth.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.AuthenticationPrincipal;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.application.AuthService;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest,
                                      HttpServletResponse response) {
        Cookie cookie = new Cookie("token", authService.createToken(loginRequest));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    @GetMapping("/check")
    public ResponseEntity<LoginMember> findMemberInformation(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(loginMember);
    }
}
