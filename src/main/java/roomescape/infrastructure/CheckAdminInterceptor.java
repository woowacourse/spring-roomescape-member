package roomescape.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.service.AuthService;
import roomescape.exception.AuthorizationException;
import roomescape.service.dto.LoginMember;
import roomescape.domain.Role;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AuthService authService;
    private final AuthenticationExtractor authenticationExtractor;

    public CheckAdminInterceptor(AuthService authService, AuthenticationExtractor authenticationExtractor) {
        this.authService = authService;
        this.authenticationExtractor = authenticationExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logger.trace("request = {}", request.getRequestURI());

        String uri = request.getRequestURI();
        String method = request.getMethod();

        if (method.equals("POST")
                && (uri.startsWith("/reservations") || uri.startsWith("/members"))) {
            return true;
        }
        if (method.equals("GET")
                && (uri.startsWith("/times") || uri.startsWith("/themes"))) {
            return true;
        }

        String token = authenticationExtractor.extract(request);
        LoginMember member = authService.findMemberByToken(token);
        if (Role.ADMIN != member.role()) {
            throw new AuthorizationException();
        }

        return true;
    }
}
