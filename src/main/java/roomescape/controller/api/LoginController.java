package roomescape.controller.api;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.token.TokenManager;
import roomescape.dto.MemberResponse;
import roomescape.dto.TokenRequest;
import roomescape.dto.TokenResponse;
import roomescape.model.LoginMember;
import roomescape.service.AuthService;

@RestController
public class LoginController {

    private final AuthService authService;
    private final TokenManager tokenManager;

    public LoginController(AuthService authService, TokenManager tokenManager) {
        this.authService = authService;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/login")
    public void login(@RequestBody final TokenRequest tokenRequest, final HttpServletResponse response) {
        final TokenResponse tokenResponse = authService.createToken(tokenRequest);
        tokenManager.setToken(response, tokenResponse);
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(final LoginMember loginMember) {
        final MemberResponse memberResponse = new MemberResponse(loginMember.getId(), loginMember.getName(), loginMember.getRole().name(), loginMember.getEmail());
        return ResponseEntity.ok(memberResponse);
    }
}
