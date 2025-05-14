package roomescape.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.exception.UnauthorizedAccessException;
import roomescape.global.security.CookieUtil;
import roomescape.global.security.JwtProvider;
import roomescape.member.domain.Role;
import roomescape.member.repository.MemberRepository;

@Component
public class AdminAuthorizationInterceptor implements HandlerInterceptor {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public AdminAuthorizationInterceptor(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = CookieUtil.extractTokenFromCookie(request.getCookies());

        Role role = Role.findBy(jwtProvider.getRoleName(token));
        if (role != Role.ADMIN) {
            throw new UnauthorizedAccessException();
        }

        return true;
    }
}
