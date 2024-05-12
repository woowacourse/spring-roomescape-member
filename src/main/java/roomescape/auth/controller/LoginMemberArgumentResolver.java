package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.provider.JwtTokenProvider;
import roomescape.auth.service.AuthService;
import roomescape.exception.AuthorizationException;
import roomescape.member.domain.Member;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginMemberArgumentResolver(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Member resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();
        validateCookie(cookies);

        String token = jwtTokenProvider.extractTokenFromCookie(cookies);

        return authService.findMemberByToken(token);
    }

    private void validateCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new AuthorizationException("쿠키가 없습니다.");
        }
    }
}
