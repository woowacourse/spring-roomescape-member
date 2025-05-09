package roomescape.login.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.common.authentication.AuthenticationPrincipal;
import roomescape.common.authorization.AuthorizationHandler;
import roomescape.common.authorization.TokenAuthorizationHandler;
import roomescape.login.dto.MemberRequest;
import roomescape.login.dto.MemberResponse;
import roomescape.login.dto.TokenResponse;
import roomescape.login.service.LoginService;

@Controller
public class LoginApiController {
    private final LoginService loginService;
    private final AuthorizationHandler<String> authorizationHandler;

    public LoginApiController(LoginService loginService) {
        this.loginService = loginService;
        this.authorizationHandler = new TokenAuthorizationHandler();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody MemberRequest memberRequest,
            HttpServletResponse httpServletResponse
    ) {
        TokenResponse tokenResponse = loginService.createToken(memberRequest);
        authorizationHandler.createCookie(tokenResponse.accessToken(), httpServletResponse);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(@AuthenticationPrincipal MemberResponse memberResponse) {
        return ResponseEntity.ok(memberResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        authorizationHandler.deleteCookie(httpServletRequest, httpServletResponse);
        return ResponseEntity.ok().build();
    }
}
