package roomescape.controller.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.service.AuthService;
import roomescape.controller.infrastructure.AuthorizationExtractor;

public class CheckLoginInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AuthService authService;
    private final AuthorizationExtractor authorizationExtractor;

    public CheckLoginInterceptor(AuthService authService, AuthorizationExtractor authorizationExtractor) {
        this.authService = authService;
        this.authorizationExtractor = authorizationExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logger.trace("request = {}", request.getRequestURI());

        String token = authorizationExtractor.extract(request);
        authService.validateToken(token);
        return true;
    }
}
