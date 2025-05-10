package roomescape.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.CheckLoginResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.LoginResponse;
import roomescape.auth.infrastructure.CookieManager;
import roomescape.auth.service.AuthService;

@RestController
public class AuthController {

    private static final String TOKEN = "token";

    private final AuthService authService;
    private final CookieManager cookieManager;

    public AuthController(final AuthService authService, final CookieManager cookieManager) {
        this.authService = authService;
        this.cookieManager = cookieManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(final HttpServletResponse httpServletResponse,
                                      final @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.createToken(loginRequest);
        cookieManager.addCookieToResponse(httpServletResponse, TOKEN, loginResponse.accessToken());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<CheckLoginResponse> checkLogin(final @CookieValue(value = TOKEN) String token) {
        CheckLoginResponse checkLoginResponse = authService.findNameByToken(token);
        return ResponseEntity.ok(checkLoginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(final HttpServletResponse httpServletResponse) {
        cookieManager.deleteCookie(httpServletResponse, TOKEN);
        return ResponseEntity.noContent().build();
    }
}
