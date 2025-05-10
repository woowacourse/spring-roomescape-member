package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.controller.dto.SignupRequest;
import roomescape.auth.controller.dto.SignupResponse;
import roomescape.auth.controller.dto.TokenRequest;
import roomescape.auth.entity.Member;
import roomescape.auth.service.AuthService;

@RestController
public class AuthApiController {

    private final AuthService authService;

    public AuthApiController(final AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/members")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
        Member member = authService.register(
                signupRequest.getName(),
                signupRequest.getEmail(),
                signupRequest.getPassword()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(SignupResponse.from(member));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody TokenRequest tokenRequest, HttpServletResponse response) {
        String jwtToken = authService.login(tokenRequest.getEmail(), tokenRequest.getPassword());

        // Set token as HttpOnly cookie
        Cookie cookie = new Cookie("token", jwtToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60); // 1 hour (optional)
        response.addCookie(cookie);

        return ResponseEntity.ok().build(); // 응답 본문 없이 200 OK만
    }
}
