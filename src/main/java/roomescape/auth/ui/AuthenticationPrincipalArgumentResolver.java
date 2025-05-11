package roomescape.auth.ui;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.application.AuthService;
import roomescape.member.application.MemberService;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthService authService;
    private final MemberService memberService;

    public AuthenticationPrincipalArgumentResolver(AuthService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Cookie[] cookies = ((HttpServletRequest) webRequest.getNativeRequest()).getCookies();
        String token = null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
            }
        }

        String memberId = authService.getSubject(token);
        return memberService.findById(Long.parseLong(memberId));
    }
}
