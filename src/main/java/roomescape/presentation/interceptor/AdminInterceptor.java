package roomescape.presentation.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.common.exception.ForbiddenException;
import roomescape.common.exception.UnauthorizedException;
import roomescape.domain.Role;
import roomescape.service.JwtProvider;
import roomescape.service.JwtProvider.JwtPayload;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    public AdminInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie tokenCookie = getTokenCookie(request);
        JwtPayload jwtPayload = jwtProvider.extractPayload(tokenCookie.getValue());
        if (jwtPayload.role() != Role.ADMIN) {
            throw new ForbiddenException("접근 권한이 없습니다.");
        }
        return true;
    }

    private static Cookie getTokenCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new UnauthorizedException("인증 쿠키값이 존재하지 않습니다.");
        }
        return Arrays.stream(request.getCookies())
                .filter(each -> each.getName().equals("token"))
                .findFirst()
                .orElseThrow(() -> new UnauthorizedException("인증 쿠키값이 존재하지 않습니다."));
    }
}
