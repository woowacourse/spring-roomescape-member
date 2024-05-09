package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.LoginMember;
import roomescape.service.AuthService;
import roomescape.service.LoginMemberService;
import roomescape.service.dto.LoginMemberNameResponse;
import roomescape.service.dto.TokenRequest;
import roomescape.service.dto.TokenResponse;

@RestController
@RequestMapping("/login")
public class TokenLoginController {
    private final AuthService authService;
    private final LoginMemberService loginMemberService;

    @Autowired
    public TokenLoginController(AuthService authService, LoginMemberService loginMemberService) {
        this.authService = authService;
        this.loginMemberService = loginMemberService;
    }

    @PostMapping
    public void tokenLogin(@RequestBody TokenRequest tokenRequest, HttpServletResponse response) {
        loginMemberService.validateLogin(tokenRequest);
        TokenResponse tokenResponse = authService.createToken(tokenRequest);
        Cookie cookie = authService.createCookieByToken(tokenResponse);
        response.addCookie(cookie);
    }

    @GetMapping("/check")
    public LoginMemberNameResponse findUser(LoginMember loginMember) {
        return new LoginMemberNameResponse(loginMember.getName());
    }
}
