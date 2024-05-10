package roomescape.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.controller.LoginController;
import roomescape.exception.AuthenticationException;
import roomescape.service.AuthService;

public class CheckLoginInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AuthService authService;
    private final AuthenticationExtractor authenticationExtractor;

    public CheckLoginInterceptor(AuthService authService, AuthenticationExtractor authenticationExtractor) {
        this.authService = authService;
        this.authenticationExtractor = authenticationExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logger.trace("request = {}", request.getRequestURI());

        String token = authenticationExtractor.extract(request);
        authService.validateToken(token);
        return true;
    }
}
