package roomescape.controller.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.application.AuthService;
import roomescape.auth.infrastructure.AuthorizationExtractor;

public class CheckLoginInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final AuthorizationExtractor authorizationExtractor;

    public CheckLoginInterceptor(AuthService authService, AuthorizationExtractor authorizationExtractor) {
        this.authService = authService;
        this.authorizationExtractor = authorizationExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("request = " + request.getRequestURI());
        String token = authorizationExtractor.extract(request);
        authService.validateToken(token);
        return true;
    }
}
