package roomescape.auth.web.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.dto.AuthenticatedMember;
import roomescape.auth.infrastructure.TokenProvider;
import roomescape.global.exception.AuthenticationException;
import roomescape.global.util.CookieUtils;
import roomescape.member.domain.Role;

@Component
@RequiredArgsConstructor
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie cookie = CookieUtils.findFromCookiesByName(request.getCookies(), "token")
                .orElseThrow(() -> new AuthenticationException("인증을 위한 쿠키가 존재하지 않습니다."));
        AuthenticatedMember authenticatedMember = tokenProvider.resolveAuthenticatedMember(cookie.getValue());
        if (Objects.equals(authenticatedMember.role(), Role.ADMIN)) {
            return true;
        }

        // TODO : HandlerExceptionResolver 커스텀 클래스 구현해 처리하기
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{\"message\": \"관리자 권한이 필요합니다.\"}");
        return false;
    }
}
