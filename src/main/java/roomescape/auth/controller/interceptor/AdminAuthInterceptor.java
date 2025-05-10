package roomescape.auth.controller.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.infrastructure.JwtPayload;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.auth.infrastructure.TokenExtractor;
import roomescape.global.exception.error.ForbiddenException;
import roomescape.global.exception.error.UnauthorizedException;
import roomescape.member.domain.enums.Role;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final TokenExtractor tokenExtractor;
    private final JwtTokenProvider tokenProvider;

    public AdminAuthInterceptor(TokenExtractor tokenExtractor, JwtTokenProvider tokenProvider) {
        this.tokenExtractor = tokenExtractor;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = tokenExtractor.extractTokenByCookies(request)
                .orElseThrow(() -> new UnauthorizedException("인증 토큰이 쿠키에 존재하지 않습니다."));

        JwtPayload payload = tokenProvider.getPayload(token);

        if (payload.role() != Role.ADMIN) {
            throw new ForbiddenException("접근 권한이 없습니다.");
        }

        return true;
    }
}
