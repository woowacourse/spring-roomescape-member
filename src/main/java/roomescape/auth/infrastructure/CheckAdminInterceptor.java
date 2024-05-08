package roomescape.auth.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.application.AuthService;
import roomescape.auth.application.AuthorizationException;
import roomescape.controller.login.LoginMember;
import roomescape.domain.Role;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AuthService authService;
    private final AuthorizationExtractor authorizationExtractor;

    public CheckAdminInterceptor(AuthService authService, AuthorizationExtractor authorizationExtractor) {
        this.authService = authService;
        this.authorizationExtractor = authorizationExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logger.trace("request = {}", request.getRequestURI());

        String token = authorizationExtractor.extract(request);
        LoginMember member = authService.findMemberByToken(token);
        if (Role.ADMIN != member.role()) {
            throw new AuthorizationException();
        }

        return true;
    }
}
