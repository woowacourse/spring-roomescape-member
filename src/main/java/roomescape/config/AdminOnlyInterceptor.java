package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.domain.Member;
import roomescape.member.service.JwtUtil;
import roomescape.member.service.MemberService;

public class AdminOnlyInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public AdminOnlyInterceptor(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        String uri = request.getRequestURI();
        if (!uri.startsWith("/admin")) {
            return true;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.sendRedirect("/login");
            return false;
        }
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                String token = cookie.getValue();
                Long memberId = jwtUtil.getMemberIdFromToken(token);
                Member member = memberService.findById(memberId);
                return member.isAdmin();
            }
        }
        response.sendRedirect("/login");
        return false;
    }
}
