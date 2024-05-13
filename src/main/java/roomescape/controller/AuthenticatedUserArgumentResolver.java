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
import roomescape.service.AuthService;
import roomescape.service.CookieService;

import java.util.Optional;

@Component
public class AuthenticatedUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final CookieService cookieService;
    private final AuthService authService;

    public AuthenticatedUserArgumentResolver(CookieService cookieService, AuthService authService) {
        this.cookieService = cookieService;
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
        Optional<Cookie> findCookie = cookieService.findCookie(request);
        if (findCookie.isEmpty()) {
            return null;
        }
        Cookie cookie = findCookie.get();
        return authService.findPayloadByToken(cookie.getValue());
    }
}
