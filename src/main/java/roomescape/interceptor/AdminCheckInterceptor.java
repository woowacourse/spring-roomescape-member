package roomescape.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.token.TokenProvider;
import roomescape.exception.AccessDeniedException;
import roomescape.member.model.MemberRole;
import roomescape.util.CookieUtil;

public class AdminCheckInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;

    public AdminCheckInterceptor(final TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws Exception {
        final MemberRole memberRole = parseMemberRole(request);

        if (memberRole != MemberRole.ADMIN) {
            throw new AccessDeniedException("유효하지 않은 권한 요청입니다.");
        }

        return true;
    }

    private MemberRole parseMemberRole(final HttpServletRequest request) {
        final String accessToken = CookieUtil.findCookie(request, "token")
                .orElseThrow(() -> new IllegalArgumentException("요청에 인증 쿠키가 존재하지 않습니다."))
                .getValue();
        final String role = tokenProvider.convertAuthenticationToken(accessToken)
                .getClaims()
                .get("role")
                .toString();

        return MemberRole.of(role);
    }
}
