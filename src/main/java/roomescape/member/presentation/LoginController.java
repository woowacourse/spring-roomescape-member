package roomescape.member.presentation;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.argumentResolver.Login;
import roomescape.common.util.TokenCookieManager;
import roomescape.member.dto.LoginCheckResponse;
import roomescape.member.dto.LoginMember;
import roomescape.member.dto.LoginRequest;
import roomescape.member.service.LoginService;

@RestController
public class LoginController {

    private final TokenCookieManager tokenCookieManager;
    private final LoginService loginService;

    public LoginController(TokenCookieManager tokenCookieManager, LoginService loginService) {
        this.tokenCookieManager = tokenCookieManager;
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String token = loginService.loginAndReturnToken(request);
        tokenCookieManager.addTokenCookie(response, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(@Login LoginMember member) {
        return ResponseEntity.ok().body(new LoginCheckResponse(member.name()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        tokenCookieManager.deleteTokenCookie(response);
        return ResponseEntity.ok().build();
    }
}
