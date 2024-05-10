package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.config.security.JwtTokenProvider;
import roomescape.exception.NotAllowRoleException;
import roomescape.member.controller.MemberLoginApiController;
import roomescape.member.dto.LoginMember;

@Component
public class RoleCheckInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public RoleCheckInterceptor(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);

        LoginMember loginMember = jwtTokenProvider.getMember(token);
        if (!loginMember.role().isAdmin()) {
            throw new NotAllowRoleException("접근 권한이 없습니다.");
        }
        return true;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(MemberLoginApiController.COOKIE_TOKEN_KEY))
                .findAny()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
