package roomescape.config.interceptor;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
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
            return checkCookieToken(request, response);
        }
        return true;
    }

    private boolean checkCookieToken(final HttpServletRequest request,
                                     final HttpServletResponse response) throws IOException {
        Optional<String> cookieOptional = CookieParser.getCookie(request, "token");
        if (cookieOptional.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유저 인증에 실패했습니다.");
            return false;
        }

        try {
            String cookie = cookieOptional.get();
            JwtRequest jwtRequest = jwtProvider.verifyToken(cookie);
            return checkAdmin(response, jwtRequest);
        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유저 인증에 실패했습니다.");
            return false;
        }
    }

    private boolean checkAdmin(final HttpServletResponse response, final JwtRequest jwtRequest)
            throws IOException {
        if (jwtRequest == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유저 인증에 실패했습니다.");
            return false;
        }

        if (jwtRequest.role() != MemberRoleType.ADMIN) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자만 접근 가능합니다.");
            return false;
        }
        return true;
    }
}
