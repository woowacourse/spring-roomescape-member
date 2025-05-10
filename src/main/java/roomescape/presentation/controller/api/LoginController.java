package roomescape.presentation.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.domain.member.Member;
import roomescape.business.service.AuthService;
import roomescape.config.AuthMember;
import roomescape.presentation.dto.LoginCheckResponse;
import roomescape.presentation.dto.LoginRequest;
import roomescape.presentation.dto.TokenResponse;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    public LoginController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void login(
            @RequestBody @Valid final LoginRequest loginRequest,
            final HttpServletResponse response
    ) {
        final TokenResponse token = authService.createToken(loginRequest);
        final Cookie cookie = new Cookie("token", token.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(@AuthMember final Member member) {
        final LoginCheckResponse response = new LoginCheckResponse(member.getName());
        return ResponseEntity.ok().body(response);
    }
}
