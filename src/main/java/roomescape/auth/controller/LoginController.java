package roomescape.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.domain.AccessTokenCookie;
import roomescape.auth.dto.LoggedInMember;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.LoginResponse;
import roomescape.auth.service.AuthService;

@RestController
public class LoginController {
    private final TokenCookieConvertor tokenCookieConvertor;
    private final AuthService authService;

    public LoginController(TokenCookieConvertor tokenCookieConvertor, AuthService authService) {
        this.tokenCookieConvertor = tokenCookieConvertor;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        String token = authService.createToken(request);
        ResponseCookie cookie = tokenCookieConvertor.createResponseCookie(token);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public LoginResponse loginCheck(LoggedInMember member) {
        String name = member.name();
        return new LoginResponse(name);
    }
}
