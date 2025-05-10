package roomescape.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import roomescape.auth.annotation.MemberPrincipal;
import roomescape.auth.domain.LoginMember;
import roomescape.auth.dto.CheckLoginResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.LoginResponse;
import roomescape.auth.infrastructure.CookieManager;
import roomescape.auth.service.AuthService;

@Controller
public class AuthController {

    private static final String TOKEN = "token";

    private final AuthService authService;
    private final CookieManager cookieManager;

    public AuthController(final AuthService authService, final CookieManager cookieManager) {
        this.authService = authService;
        this.cookieManager = cookieManager;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Void> login(final @RequestBody LoginRequest loginRequest,
                                      final HttpServletResponse httpServletResponse) {
        LoginResponse loginResponse = authService.createToken(loginRequest);
        cookieManager.addCookieToResponse(httpServletResponse, TOKEN, loginResponse.accessToken());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    @ResponseBody
    public ResponseEntity<CheckLoginResponse> checkLogin(final @MemberPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(CheckLoginResponse.from(loginMember));
    }

    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<Void> logout(final @MemberPrincipal LoginMember loginMember,
                                       final HttpServletResponse httpServletResponse) {
        cookieManager.deleteCookie(httpServletResponse, TOKEN);
        return ResponseEntity.noContent().build();
    }
}
