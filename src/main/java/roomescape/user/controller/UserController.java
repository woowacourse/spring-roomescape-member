package roomescape.user.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.user.service.AuthService;
import roomescape.user.service.dto.LoginInfo;
import roomescape.user.service.dto.TokenResponse;

@RestController
public class UserController {

    private final AuthService authService;

    public UserController(final AuthService authService) {
        this.authService = authService;
    }

    /**
     * TODO
     * 이미 쿠키에 토큰이 존재한다면?
     */
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginInfo request) {
        TokenResponse tokenResponse = authService.tokenLogin(request);
        ResponseCookie cookie = ResponseCookie.from("token", tokenResponse.token())
                .httpOnly(true)
                .path("/")
                .build();
        return ResponseEntity.ok().header("Set-Cookie", cookie.toString()).build();
    }
}
