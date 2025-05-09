package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Member;
import roomescape.domain.enums.Role;
import roomescape.service.member.MemberServiceImpl;
import roomescape.util.CookieManager;
import roomescape.util.JwtTokenProvider;

public class AdminInterceptor implements HandlerInterceptor {

    private final MemberServiceImpl memberServiceImpl;

    public AdminInterceptor(MemberServiceImpl memberServiceImpl) {
        this.memberServiceImpl = memberServiceImpl;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie[] cookies = request.getCookies();
        String jwtToken = CookieManager.extractTokenFromCookies(cookies);
        Long id = JwtTokenProvider.findMemberIdByToken(jwtToken);
        Member member = memberServiceImpl.findMemberById(id);
        if (member == null || !member.getRole().equals(Role.ADMIN)) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
