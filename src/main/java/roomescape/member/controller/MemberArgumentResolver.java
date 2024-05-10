package roomescape.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.handler.exception.CustomBadRequest;
import roomescape.handler.exception.CustomException;
import roomescape.member.domain.Member;
import roomescape.member.service.AuthService;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public MemberArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        if (request.getCookies() == null) {
            throw new CustomException(CustomBadRequest.NOT_LOGIN);
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("token")) {
                String email = authService.parseEmail(cookie);
                return authService.findByEmail(email);
            }
        }
        throw new CustomException(CustomBadRequest.NO_LOGIN_TOKEN);
    }
}
