package roomescape.infrastructure;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.business.Member;
import roomescape.business.service.AuthenticationService;

@Component
public class AdminAuthorityInterceptor implements HandlerInterceptor {

    private final AuthenticationService authenticationService;

    @Autowired
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
        return member.isAdmin();
    }
}
