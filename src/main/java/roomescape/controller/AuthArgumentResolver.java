package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.service.MemberService;
import roomescape.util.JwtTokenProvider;

public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthArgumentResolver(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest nativeRequest = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = nativeRequest.getCookies();
        String token = extractTokenFromCookie(cookies);
        jwtTokenProvider.validateToken(token);
        Long id = jwtTokenProvider.extractId(token);
        Member loginMember = memberService.findLoginMemberById(id);
        return new LoginMember(loginMember.getId(), loginMember.getName(), loginMember.getEmail(), loginMember.getRole());
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
