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
import roomescape.member.presentation.dto.response.MemberResponse;

@RestController
@RequestMapping("/login")
public class TokenLoginController {
    private final AuthService authService;

    public TokenLoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(
            @Valid @RequestBody TokenRequest tokenRequest,
            HttpServletResponse response) {

        TokenResponse tokenResponse = authService.createToken(tokenRequest);

        Cookie cookie = new Cookie("token", tokenResponse.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION, "/")
                .build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> checkMember(@AuthenticatedMember LoginMember member) {
        MemberResponse memberResponse = new MemberResponse(member.id(), member.name(), member.email());
        return ResponseEntity.ok(memberResponse);
    }
}
