package roomescape.presentation.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.application.JwtPayload;
import roomescape.common.exception.ForbiddenException;
import roomescape.domain.Role;
import roomescape.infrastructure.security.JwtProvider;
import roomescape.presentation.JwtTokenExtractor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtTokenExtractor jwtTokenExtractor;
    private final JwtProvider jwtProvider;

    public AdminInterceptor(JwtTokenExtractor jwtTokenExtractor, JwtProvider jwtProvider) {
        this.jwtTokenExtractor = jwtTokenExtractor;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String jwtToken = jwtTokenExtractor.extract(request);
        JwtPayload jwtPayload = jwtProvider.extractPayload(jwtToken);
        if (jwtPayload.role() != Role.ADMIN) {
            throw new ForbiddenException("접근 권한이 없습니다.");
        }
        return true;
    }
}
