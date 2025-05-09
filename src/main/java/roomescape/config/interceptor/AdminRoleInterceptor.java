package roomescape.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.annotation.AdminOnly;
import roomescape.domain.MemberRoleType;
import roomescape.jwt.JwtProvider;
import roomescape.jwt.JwtRequest;
import roomescape.util.CookieParser;

@Component
public class AdminRoleInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    public AdminRoleInterceptor(final JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        boolean onMethod = handlerMethod.hasMethodAnnotation(AdminOnly.class);
        boolean onClass = handlerMethod.getBeanType().isAnnotationPresent(AdminOnly.class);

        if (onMethod || onClass) {
            String tokenCookie = CookieParser.getTokenCookie(request, "token");
            JwtRequest jwtRequest = jwtProvider.verifyToken(tokenCookie);
            return checkAdmin(response, jwtRequest);
        }
        return true;
    }

    private static boolean checkAdmin(final HttpServletResponse response,
                                      final JwtRequest jwtRequest) throws IOException {
        if (jwtRequest == null || jwtRequest.role() != MemberRoleType.ADMIN) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자만 접근 가능합니다.");
            return false;
        }
        return true;
    }
}
