package roomescape.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.infrastructure.authentication.AuthService;
import roomescape.infrastructure.authentication.AuthenticatedMemberProfile;
import roomescape.infrastructure.authentication.AuthenticationRequest;
import roomescape.infrastructure.authentication.UnauthorizedException;
import roomescape.web.security.CookieTokenExtractor;

@RestController
@RequestMapping("/login")
class AuthController {

    private final AuthService authService;
    private final CookieTokenExtractor extractor;

    public AuthController(AuthService authService, CookieTokenExtractor extractor) {
        this.authService = authService;
        this.extractor = extractor;
    }

    @PostMapping
    public ResponseEntity<Void> login(
            @Valid @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ) {
        String token = authService.authenticate(request);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<AuthenticatedMemberProfile> check(HttpServletRequest request) {
        String token = extractor.extract(request);
        if (token == null) {
            throw new UnauthorizedException();
        }

        return ResponseEntity.ok(authService.authorize(token));
    }
}
