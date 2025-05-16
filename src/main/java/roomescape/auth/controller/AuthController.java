package roomescape.auth.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.controller.dto.MemberNameResponse;
import roomescape.auth.service.MemberService;
import roomescape.auth.service.dto.LoginMember;
import roomescape.auth.service.dto.request.LoginRequest;
import roomescape.auth.service.dto.response.LoginResponse;
import roomescape.global.infrastructure.AuthTokenCookieProvider;

@RestController
@RequestMapping
public class AuthController {
    private final MemberService service;
    private final AuthTokenCookieProvider authTokenCookieProvider;

    public AuthController(MemberService service, AuthTokenCookieProvider authTokenCookieProvider) {
        this.service = service;
        this.authTokenCookieProvider = authTokenCookieProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse loginResponse = service.login(request);
        ResponseCookie cookie = authTokenCookieProvider.generate(loginResponse.token());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberNameResponse> checkLogin(LoginMember member) {
        return ResponseEntity.ok(new MemberNameResponse(member.name()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = authTokenCookieProvider.generateExpired();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
