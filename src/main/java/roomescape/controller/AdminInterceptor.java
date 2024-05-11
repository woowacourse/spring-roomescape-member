package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final MemberService memberService;

    public AdminInterceptor(final AuthService authService, final MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            response.setStatus(401);
            return false;
//            throw new IllegalArgumentException("권한이 없습니다.");
        }
        String token = extractTokenFromCookie(cookies);
        Long id = authService.findByToken(token);
        if(!memberService.findById(id).getRole().equals("admin")){
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
                           final ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";
    }
}
