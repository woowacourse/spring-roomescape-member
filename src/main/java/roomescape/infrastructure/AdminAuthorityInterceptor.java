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

@Named
public class AdminAuthorityInterceptor implements HandlerInterceptor {

    private final AuthenticationService authenticationService;

    @Inject
    public AdminAuthorityInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.setStatus(401);
            return false;
        }
        Optional<Cookie> token = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst();
        if (token.isEmpty()) {
            response.setStatus(401);
            return false;
        }
        Member member = authenticationService.findMemberByToken(token.get().getValue());
        if (!member.isAdmin()) {
            response.setStatus(401);
            return false;
        }
        return true;

    }
}
