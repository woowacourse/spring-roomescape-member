package roomescape.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.auth.AuthService;
import roomescape.service.auth.AuthenticatedMemberProfile;
import roomescape.service.auth.AuthenticationRequest;
import roomescape.web.security.Authenticated;
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
    public ResponseEntity<AuthenticatedMemberProfile> check(
            @Authenticated AuthenticatedMemberProfile profile
    ) {
        return ResponseEntity.ok(profile);
    }
}
