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
@RequestMapping("/login")
public class LoginController {
    private final MemberAuthService service;
    private final AuthTokenCookieProvider authTokenCookieProvider;

    public LoginController(MemberAuthService service) {
        this.service = service;
        this.authTokenCookieProvider = authTokenCookieProvider;
    }

    @PostMapping
    public void login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = service.login(request);
        Cookie cookie = authTokenCookieProvider.generate(loginResponse.token());
        response.addCookie(cookie);
    }

    @GetMapping("/check")
    public ResponseEntity<MemberNameResponse> checkLogin(LoginMember member) {
        return ResponseEntity.ok(new MemberNameResponse(member.name()));
    }
}
