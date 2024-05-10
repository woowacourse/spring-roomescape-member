package roomescape.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.principal.AuthenticatedMember;
import roomescape.auth.service.AuthService;
import roomescape.auth.token.AuthenticationToken;
import roomescape.resolver.Authenticated;
import roomescape.util.CookieUtil;

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

    @GetMapping("/login/check")
    public LoginCheckResponse checkLogin(@Authenticated AuthenticatedMember authenticatedMember) {
        return new LoginCheckResponse(authenticatedMember.name());
    }
}
