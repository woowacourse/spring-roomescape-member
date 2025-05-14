package roomescape.domain.auth.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.auth.config.AuthenticationPrincipal;
import roomescape.domain.auth.config.JwtProperties;
import roomescape.domain.auth.dto.LoginRequest;
import roomescape.domain.auth.dto.LoginUserDto;
import roomescape.domain.auth.dto.TokenResponse;
import roomescape.domain.auth.dto.UserInfoResponse;
import roomescape.domain.auth.service.AuthService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final String cookieKey;
    private final AuthService authService;

    @Autowired
    public LoginController(final JwtProperties jwtProperties, final AuthService authService) {
        this.cookieKey = jwtProperties.getCookieKey();
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@Valid @RequestBody final LoginRequest loginRequest) {
        final TokenResponse tokenResponse = authService.login(loginRequest);

        final ResponseCookie cookie = ResponseCookie.from(cookieKey, tokenResponse.token())
                .httpOnly(true)
                .maxAge(60 * 60)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/check")
    public ResponseEntity<UserInfoResponse> check(@AuthenticationPrincipal final LoginUserDto loginUserDto) {
        final UserInfoResponse response = authService.getUserInfo(loginUserDto);

        return ResponseEntity.ok(response);
    }
}
