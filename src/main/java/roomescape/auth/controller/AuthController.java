package roomescape.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.auth.dto.response.CheckMemberResponse;
import roomescape.auth.dto.response.LoginResponse;
import roomescape.auth.service.AuthService;

@RestController
public class AuthController {
    private static final String TOKEN_COOKIE_NAME = "token";

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginMemberRequest) {
        LoginResponse loginResponse = authService.login(loginMemberRequest);
        ResponseCookie responseCookie = ResponseCookie.from(TOKEN_COOKIE_NAME, loginResponse.token())
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<CheckMemberResponse> checkLogin(@CookieValue(name = TOKEN_COOKIE_NAME) String token) {
        CheckMemberResponse checkMemberResponse = authService.checkMember(token);
        return ResponseEntity.ok(checkMemberResponse);
    }
}
