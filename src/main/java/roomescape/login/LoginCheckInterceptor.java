package roomescape.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import javax.naming.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.service.MemberService;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    public LoginCheckInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws AuthenticationException {
        String token = extractTokenFromCookie(request.getCookies());
        if (memberService.isAdminToken(token)) {
            return true;
        }
        throw new AuthenticationException("접근 권한이 없습니다.");
    }

    private String extractTokenFromCookie(Cookie[] cookies) throws AuthenticationException {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Objects.equals(cookie.getName(), "token")) {
                    return cookie.getValue();
                }
            }
        }
        throw new AuthenticationException("접근 권한 확인을 위한 쿠키가 없습니다.");
    }
}
