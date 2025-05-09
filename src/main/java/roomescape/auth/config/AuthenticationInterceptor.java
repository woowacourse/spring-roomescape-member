package roomescape.auth.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.AuthRequired;
import roomescape.auth.jwt.JwtUtil;
import roomescape.business.model.vo.LoginInfo;
import roomescape.exception.auth.NotAuthenticatedException;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public AuthenticationInterceptor(final JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        if (isAuthenticationNotRequired(handler)) return true;
        String token = extractTokenFromCookies(request);
        LoginInfo loginInfo = jwtUtil.validateAndResolveToken(token);
        request.setAttribute("authorization", loginInfo);
        return true;
    }

    private static boolean isAuthenticationNotRequired(final Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        AuthRequired authRequired = handlerMethod.getMethodAnnotation(AuthRequired.class);
        return authRequired == null;
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        throw new NotAuthenticatedException();
    }
}
