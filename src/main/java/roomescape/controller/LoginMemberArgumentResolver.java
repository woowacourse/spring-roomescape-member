package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.member.Member;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private MemberService memberService;
    private AuthService authService;

    public LoginMemberArgumentResolver(final MemberService memberService, final AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Member resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);
        Long number = authService.findByToken(token);
        Member member = memberService.findById(number);

        return member;
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
