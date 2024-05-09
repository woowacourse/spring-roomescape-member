package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.dto.response.AuthResponse;
import roomescape.dto.response.MemberResponse;
import roomescape.service.AuthService;

import java.util.Arrays;
import java.util.Optional;

@Component
public class AuthenticatedUserArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String TOKEN = "token";

    private final AuthService authService;

    public AuthenticatedUserArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(AuthenticatedUser.class);
        boolean hasMemberResponseType = AuthResponse.class.isAssignableFrom(parameter.getParameterType());
        return hasParameterAnnotation && hasMemberResponseType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Optional<Cookie> findCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(TOKEN))
                .findAny();
        if (findCookie.isEmpty()) {
            return null;
        }
        Cookie cookie = findCookie.get();
        return authService.findPayloadByToken(cookie.getValue());
    }
}
