package roomescape.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.MemberResponse;
import roomescape.dto.TokenRequest;
import roomescape.dto.TokenResponse;
import roomescape.model.LoginMember;
import roomescape.service.AuthService;

@RestController
public class LoginController {

    private static final String TOKEN_FIELD = "token";

    private final AuthService authService;

    public LoginController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public void login(@RequestBody final TokenRequest tokenRequest, final HttpServletResponse response) {
        final TokenResponse tokenResponse = authService.createToken(tokenRequest);
        final Cookie cookie = new Cookie(TOKEN_FIELD, tokenResponse.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(final LoginMember loginMember) {
        final MemberResponse memberResponse = new MemberResponse(loginMember.getId(), loginMember.getName(), loginMember.getRole().name(), loginMember.getEmail());
        return ResponseEntity.ok(memberResponse);
    }
}
