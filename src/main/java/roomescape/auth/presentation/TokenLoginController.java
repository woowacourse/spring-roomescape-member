package roomescape.auth.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.application.AuthService;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.dto.TokenResponse;
import roomescape.auth.infrastructure.CookieProvider;
import roomescape.member.presentation.dto.response.MemberResponse;

@RestController
@RequestMapping("/login")
public class TokenLoginController {

    private final AuthService authService;
    private final CookieProvider cookieProvider;

    public TokenLoginController(AuthService authService, CookieProvider cookieProvider) {
        this.authService = authService;
        this.cookieProvider = cookieProvider;
    }

    @PostMapping
    public ResponseEntity<Void> login(
            @Valid @RequestBody TokenRequest tokenRequest,
            HttpServletResponse response) {

        TokenResponse tokenResponse = authService.createToken(tokenRequest);

        Cookie cookie = cookieProvider.createTokenCookie(tokenResponse.accessToken());
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION, "/")
                .build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> checkMember(@AuthenticatedMember LoginMember member) {
        MemberResponse memberResponse = new MemberResponse(member.name());
        return ResponseEntity.ok(memberResponse);
    }
}
