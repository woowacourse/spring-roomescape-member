package roomescape.controller.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.configuration.AuthenticationPrincipal;
import roomescape.domain.member.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.service.auth.AuthService;

@RestController
public class TokenAuthController {
    private final AuthService authService;

    public TokenAuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, final HttpServletResponse response) {
        Cookie cookie = new Cookie("token", authService.createToken(loginRequest));
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> getUserInformation(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MemberResponse(member.getNameValue()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(final HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }
}
