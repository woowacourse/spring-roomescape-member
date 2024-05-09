package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.service.AuthService;
import roomescape.member.domain.Member;

import java.util.List;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthService authService;

    public LoginMemberArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = extractTokenFromCookie(webRequest);

        return authService.readByToken(token);
    }

    private String extractTokenFromCookie(NativeWebRequest webRequest) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        List<Cookie> cookies = List.of(request.getCookies());

        return cookies.stream()
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse("");
    }
}
