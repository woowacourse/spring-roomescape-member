package roomescape.controller.intereceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import roomescape.service.AuthenticationService;
import roomescape.service.MemberService;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final AuthenticationService authService;
    private final MemberService memberService;

    public AdminInterceptor(AuthenticationService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        Cookie[] cookies = Objects.requireNonNull(request.getCookies(), "쿠키가 비어있습니다.");
        String token = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .map(Cookie::getValue)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("토큰이 존재하지 않습니다."));
        Long id = authService.findByToken(token);
        if (id == null || !memberService.findById(id).isAdmin()) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
