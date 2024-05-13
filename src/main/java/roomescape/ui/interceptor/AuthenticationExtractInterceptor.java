package roomescape.ui.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;
import roomescape.application.AuthService;
import roomescape.auth.Principal;
import roomescape.auth.exception.AuthorizationException;

public class AuthenticationExtractInterceptor implements HandlerInterceptor {
    public static final String PRINCIPAL_KEY_NAME = "principal";
    private static final String AUTHENTICATION_KEY_NAME = "token";

    private final AuthService authService;

    public AuthenticationExtractInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie authentication = WebUtils.getCookie(request, AUTHENTICATION_KEY_NAME);
        if (authentication == null) {
            throw new AuthorizationException("인증 정보가 없습니다.");
        }
        String token = authentication.getValue();
        Principal principal = authService.createPrincipal(token);
        request.setAttribute(PRINCIPAL_KEY_NAME, principal);
        return true;
    }
}
