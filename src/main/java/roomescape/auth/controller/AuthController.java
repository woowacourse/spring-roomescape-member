package roomescape.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.domain.AuthInfo;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.auth.dto.response.GetAuthInfoResponse;
import roomescape.auth.dto.response.LoginResponse;
import roomescape.auth.service.AuthService;
import roomescape.common.AuthenticationPrincipal;

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
    public ResponseEntity<GetAuthInfoResponse> getMemberAuthInfo(@AuthenticationPrincipal AuthInfo authInfo) {
        GetAuthInfoResponse getAuthInfoResponse = authService.getMemberAuthInfo(authInfo);
        return ResponseEntity.ok(getAuthInfoResponse);
    }
}
