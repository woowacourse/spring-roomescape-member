package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.AuthService;
import roomescape.service.LoginMemberService;
import roomescape.service.dto.MemberNameResponse;
import roomescape.service.dto.MemberResponse;
import roomescape.service.dto.TokenRequest;
import roomescape.service.dto.TokenResponse;

@RestController
@RequestMapping("/login")
public class TokenLoginController {
    private final AuthService authService;
    private final LoginMemberService loginMemberService;

    public TokenLoginController(AuthService authService, LoginMemberService loginMemberService) {
        this.authService = authService;
        this.loginMemberService = loginMemberService;
    }

    @PostMapping
    public void tokenLogin(@RequestBody @Valid TokenRequest tokenRequest, HttpServletResponse response) {
        loginMemberService.validateLogin(tokenRequest);
        TokenResponse tokenResponse = authService.createToken(tokenRequest);
        Cookie cookie = authService.createCookieByToken(tokenResponse);
        response.addCookie(cookie);
    }

    @GetMapping("/check")
    public MemberNameResponse findUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        TokenResponse tokenResponse = authService.extractTokenByCookies(cookies);
        String memberId = authService.extractMemberIdByToken(tokenResponse);
        MemberResponse memberResponse = loginMemberService.findById(Long.parseLong(memberId));
        return new MemberNameResponse(memberResponse.name());
    }
}
