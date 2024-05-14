package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.service.MemberService;

public class CheckRoleInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    CheckRoleInterceptor(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        boolean isAdmin = false;
        boolean tokenAdmin = false;

        boolean isAdmin = false;
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                String subject = jwtTokenProvider.getSubject(cookie.getValue());
                long id = Long.parseLong(subject);
                Member member = memberService.getUserById(id);
                isAdmin = member.getRole().equals(Role.ADMIN);
                String role = jwtTokenProvider.getClaim(cookie.getValue(), "role", String.class);
                tokenAdmin = Role.findByName(role).equals(Role.ADMIN);
            }
        }

        if (!(isAdmin && tokenAdmin)) {
            throw new ForbiddenAccessException("관리자만 접근 가능합니다.");
        }
        return true;
    }
}
