package roomescape.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.AuthService;
import roomescape.service.InvalidAuthenticationException;
import roomescape.service.request.AuthenticationRequest;

@RestController
class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationRequest> login(
            @Valid @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ) {
        boolean authenticated = authService.verify(request);

        if (authenticated) {
            Cookie cookie = new Cookie("token", null);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok(request);
        }

        throw new InvalidAuthenticationException();
    }
}
