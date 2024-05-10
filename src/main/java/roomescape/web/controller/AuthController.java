package roomescape.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.core.dto.LoginRequestDto;
import roomescape.core.service.AuthService;
import roomescape.web.infrastructure.CookieManager;

@RestController
public class AuthController {
    private final AuthService authService;
    private final CookieManager cookieManager;

    public AuthController(final AuthService authService, final CookieManager cookieManager) {
        this.authService = authService;
        this.cookieManager = cookieManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody @Valid final LoginRequestDto request,
            final HttpServletResponse response
    ) {
        final String token = authService.createToken(request);
        final Cookie cookie = cookieManager.createCookie(token);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
