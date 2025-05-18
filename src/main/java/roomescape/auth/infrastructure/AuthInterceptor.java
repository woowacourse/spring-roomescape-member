package roomescape.auth.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.infrastructure.jwt.JwtHandler;
import roomescape.auth.infrastructure.jwt.JwtPayload;
import roomescape.auth.infrastructure.jwt.JwtProvider;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final JwtHandler jwtHandler;

    public AuthInterceptor(JwtProvider jwtProvider, JwtHandler jwtHandler) {
        this.jwtProvider = jwtProvider;
        this.jwtHandler = jwtHandler;
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) {
        Optional<String> jwt = jwtHandler.parseJwt(request);

        if (jwt.isEmpty()) {
            request.setAttribute(AuthorizationContext.ATTRIBUTE_NAME, null);
            return true;
        }

        JwtPayload jwtPayload = jwtProvider.getPayload(jwt.get());
        request.setAttribute(AuthorizationContext.ATTRIBUTE_NAME, AuthorizationContext.fromJwtPayload(jwtPayload));
        return true;
    }
}
