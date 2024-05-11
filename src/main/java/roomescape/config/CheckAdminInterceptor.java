package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.service.MemberService;
import roomescape.utils.CookieParser;
import roomescape.utils.TokenManager;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {
    private final TokenManager tokenManager;
    private final MemberService memberService;

    public CheckAdminInterceptor(TokenManager tokenManager, MemberService memberService) {
        this.tokenManager = tokenManager;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = CookieParser.searchValueFromKey(request.getCookies(), "auth_token");
        long memberId = tokenManager.getMemberIdFromToken(token);
        if (memberService.checkAdmin(memberId)) {
            return true;
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return false;
    }
}
