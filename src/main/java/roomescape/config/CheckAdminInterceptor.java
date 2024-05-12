package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Role;
import roomescape.infrastructure.AuthenticationExtractor;
import roomescape.service.AuthService;
import roomescape.service.dto.LoginMember;

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

        String token = authenticationExtractor.extract(request, LoginController.TOKEN_NAME);
        LoginMember member = authService.findMemberByToken(token);
        if (Role.ADMIN != member.role()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return false;
        }

        return true;
    }
}
