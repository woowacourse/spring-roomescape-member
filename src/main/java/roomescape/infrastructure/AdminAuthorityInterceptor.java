package roomescape.infrastructure;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.business.Member;
import roomescape.business.service.AuthenticationService;
import roomescape.exception.AuthException;

@Named
public class AdminAuthorityInterceptor implements HandlerInterceptor {

    private final AuthenticationService authenticationService;

    @Inject
    public AdminAuthorityInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthException("토큰이 존재하지 않습니다.");
        }
        Optional<Cookie> token = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst();
        if (token.isEmpty()) {
            throw new AuthException("토큰이 존재하지 않습니다.");
        }
        Member member = authenticationService.findMemberByToken(token.get().getValue());
        if (!member.isAdmin()) {
            throw new AuthException("토큰이 존재하지 않습니다.");
        }
        return true;
    }
}
