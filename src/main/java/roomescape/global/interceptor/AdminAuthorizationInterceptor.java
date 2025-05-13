package roomescape.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.global.security.CookieUtil;
import roomescape.global.security.JwtProvider;
import roomescape.member.domain.Role;
import roomescape.member.exception.MemberRoleNotExistsException;
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

        Long memberId = jwtProvider.getMemberId(token);

        Role role = memberRepository.findRoleById(memberId)
                .orElseThrow(MemberRoleNotExistsException::new);

        if (role != Role.ADMIN) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }
}
