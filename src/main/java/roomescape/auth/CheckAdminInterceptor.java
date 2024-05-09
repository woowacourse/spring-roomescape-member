package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.dto.MemberResponse;
import roomescape.exception.AuthenticationException;
import roomescape.exception.AuthorizationException;
import roomescape.model.Role;
import roomescape.service.AuthService;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private static final String TOKEN_FIELD = "token";

    private final AuthService authService;

    public CheckAdminInterceptor(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        final String token = extractTokenFromRequestCookie(request)
                .orElseThrow(() -> new AuthenticationException("토큰 정보가 존재하지 않습니다."));
        final MemberResponse memberResponse = authService.findMemberByToken(token);
        final Role memberRole = Role.from(memberResponse.role());
        if (Role.ADMIN != memberRole) {
            throw new AuthorizationException("관리자 권한이 없는 사용자입니다.");
        }
        return true;
    }

    private Optional<String> extractTokenFromRequestCookie(final HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> TOKEN_FIELD.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
