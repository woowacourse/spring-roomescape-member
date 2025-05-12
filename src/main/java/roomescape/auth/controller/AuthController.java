package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.controller.dto.MemberNameResponse;
import roomescape.auth.service.MemberAuthService;
import roomescape.auth.service.dto.request.LoginRequest;
import roomescape.auth.service.dto.response.LoginMember;
import roomescape.auth.service.dto.response.LoginResponse;
import roomescape.global.infrastructure.AuthTokenCookieProvider;

@RestController
@RequestMapping
public class AuthController {
    private final MemberAuthService service;
    private final AuthTokenCookieProvider authTokenCookieProvider;

    public AuthController(MemberAuthService service, AuthTokenCookieProvider authTokenCookieProvider) {
        this.service = service;
        this.authTokenCookieProvider = authTokenCookieProvider;
    }

    @PostMapping("/login")
    public void login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = service.login(request);
        Cookie cookie = authTokenCookieProvider.generate(loginResponse.token());
        response.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberNameResponse> checkLogin(LoginMember member) {
        return ResponseEntity.ok(new MemberNameResponse(member.name()));
    }
}
