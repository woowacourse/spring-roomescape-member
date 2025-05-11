package roomescape.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.jwt.JwtProvider;
import roomescape.util.CookieParser;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    public AuthInterceptor(final JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) {

        String tokenCookie = CookieParser.getTokenCookie(request, "token");
        jwtProvider.verifyToken(tokenCookie);

        return true;
    }
}