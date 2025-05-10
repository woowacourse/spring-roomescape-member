package roomescape.configuration;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.JwtProvider;
import roomescape.dto.request.LoginCheckRequest;
import roomescape.entity.Role;
import roomescape.service.MemberService;

public class AdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public AdminInterceptor(MemberService memberService, JwtProvider jwtProvider) {
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractTokenFromCookie(request.getCookies());

        if (token.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 정보가 없습니다.");
            return false;
        }

        Long memberId = jwtProvider.extractMemberId(token);
        LoginCheckRequest login = memberService.findById(memberId);
        if (login == null || login.role() != Role.ADMIN) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return true;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) return "";

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
