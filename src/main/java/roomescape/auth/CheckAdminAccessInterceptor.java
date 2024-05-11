package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;

public class CheckAdminAccessInterceptor implements HandlerInterceptor {
    private static final String ADMIN_INFO_KEY_NAME = "token";

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public CheckAdminAccessInterceptor(TokenProvider tokenProvider, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie adminInfo = WebUtils.getCookie(request, ADMIN_INFO_KEY_NAME);
        if (adminInfo == null) {
            throw new AccessDeniedException("잘못된 접근");
        }
        String token = adminInfo.getValue();
        long memberId = tokenProvider.extractMemberId(token);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AccessDeniedException("잘못된 접근"));
        if (!member.isAdmin()) {
            throw new AccessDeniedException("잘못된 접근");
        }
        return true;
    }
}
