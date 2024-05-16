package roomescape.controller.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Date;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.infrastructure.TokenCookieProvider;
import roomescape.infrastructure.TokenExtractor;
import roomescape.service.dto.login.LoginRequest;
import roomescape.service.dto.login.MemberNameResponse;
import roomescape.service.security.AuthService;

@RestController
@RequestMapping("/login")
@Permission(role = Role.GUEST)
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest) {
        ResponseCookie cookie = TokenCookieProvider.createCookie(authService.createToken(loginRequest));

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberNameResponse> checkLogin(HttpServletRequest request) {
        String token = TokenExtractor.extract(request);
        if (token.isBlank()) {
            return ResponseEntity.noContent().build();
        }

        Member member = authService.findMemberByValidToken(token);
        return ResponseEntity.ok()
                .header(HttpHeaders.DATE, new Date().toString())
                .body(MemberNameResponse.of(member));
    }
}
