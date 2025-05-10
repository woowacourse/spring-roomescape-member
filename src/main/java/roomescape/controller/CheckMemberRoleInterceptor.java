package roomescape.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.common.exception.AuthorizationException;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.service.JwtTokenProvider;
import roomescape.service.MemberService;

public class CheckMemberRoleInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorizationExtractor extractor;
    private final MemberService memberService;

    public CheckMemberRoleInterceptor(JwtTokenProvider jwtTokenProvider, AuthorizationExtractor extractor, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.extractor = extractor;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractor.extractTokenFromCookie(request);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException("인증 정보가 올바르지 않습니다.");
        }
        Long memberId = Long.parseLong(jwtTokenProvider.getSub(token));
        String role = jwtTokenProvider.getPayloadByKey(token, "role");
        if (!memberService.isExistMemberById(memberId) || !MemberRole.from(role).equals(MemberRole.ADMIN)) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
