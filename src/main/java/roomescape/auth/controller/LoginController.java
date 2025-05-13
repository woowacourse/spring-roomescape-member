package roomescape.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.dto.TokenResponse;
import roomescape.auth.dto.TokenWithCookieResponse;
import roomescape.auth.service.AuthService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest tokenRequest) {
        TokenWithCookieResponse tokenWithCookie = authService.issueToken(tokenRequest);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, tokenWithCookie.getCookie().toString())
                .body(new TokenResponse(tokenWithCookie.getAccessToken()));
    }

    @GetMapping("/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(LoginMember loginMember) {
        LoginCheckResponse loginCheckResponse = authService.checkLogin(loginMember);

        return ResponseEntity.ok(loginCheckResponse);
    }

}
