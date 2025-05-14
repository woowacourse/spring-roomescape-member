package roomescape.auth.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.service.AuthService;
import roomescape.error.ForbiddenException;
import roomescape.error.NotFoundException;
import roomescape.error.UnauthorizedException;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        if (isNotControllerMethod(handler)) {
            return true;
        }

        try {
            final LoginMember loginMember = authService.extractMemberByRequest(request);
            if (loginMember.isAdmin()) {
                throw new ForbiddenException("관리자 권한이 필요합니다.");
            }
        } catch (IllegalArgumentException | NotFoundException e) {
            log.error(e.getMessage());
            throw new UnauthorizedException("인증에 실패했습니다.");
        }
        return true;
    }

    private boolean isNotControllerMethod(final Object handler) {
        return !(handler instanceof HandlerMethod);
    }
}
