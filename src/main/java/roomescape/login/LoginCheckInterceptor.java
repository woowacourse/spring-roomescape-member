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
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);
        if (token != null && memberService.isAdmin(token)) {
            return true;
        }
        throw new AuthenticationException("접근 권한이 없습니다.");
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Objects.equals(cookie.getName(), "token")) {
                    return cookie.getValue();
                }
            }
        }
        return "";
    }
}
