package roomescape.config.interceptor;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
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
                             final Object handler) throws IOException {

        Optional<String> cookieOptional = CookieParser.getCookie(request, "token");
        if (cookieOptional.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증에 실패했습니다");
            return false;
        }

        try {
            String cookie = cookieOptional.get();
            jwtProvider.verifyToken(cookie);
            return true;
        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            return false;
        }
    }
}
