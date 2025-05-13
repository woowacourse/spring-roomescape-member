package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.controller.dto.MemberNameResponse;
import roomescape.auth.service.MemberService;
import roomescape.auth.service.dto.request.LoginRequest;
import roomescape.auth.service.dto.LoginMember;
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
    public void login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = service.login(request);
        Cookie cookie = authTokenCookieProvider.generate(loginResponse.token());
        response.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberNameResponse> checkLogin(LoginMember member) {
        return ResponseEntity.ok(new MemberNameResponse(member.name()));
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = authTokenCookieProvider.generateExpired();
        response.addCookie(cookie);
    }
}
