package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.service.AuthService;
import roomescape.auth.token.AuthenticationToken;
import roomescape.auth.util.CookieUtil;
import roomescape.member.model.Member;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public void login(
            @RequestBody final LoginRequest loginRequest,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        final AuthenticationToken authenticationToken = authService.login(loginRequest);

        final int cookieMaxAge = 60 * 60 * 24;
        final String cookieName = "token";
        CookieUtil.deleteCookie(request, response, cookieName);
        CookieUtil.addCookie(response, cookieName, authenticationToken.getValue(), cookieMaxAge);
    }

    // TODO : 인증 토큰 검증 추가
    @GetMapping("/login/check")
    public LoginCheckResponse checkLogin(final HttpServletRequest request) {
        final String cookieName = "token";
        final Cookie cookie = CookieUtil.findCookie(request, cookieName)
                .orElseThrow(() -> new IllegalArgumentException("요청에 인증 쿠키가 존재하지 않습니다."));
        final Member member = authService.findAuthenticatedMember(cookie.getValue());

        return LoginCheckResponse.from(member);
    }
}
