package roomescape.config.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.controller.TokenCookieConvertor;
import roomescape.auth.dto.LoggedInMember;
import roomescape.auth.dto.RequestCookies;
import roomescape.auth.service.AuthService;

@Component
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {
    private final TokenCookieConvertor tokenCookieConvertor;
    private final AuthService authService;

    public AuthenticationArgumentResolver(TokenCookieConvertor tokenCookieConvertor, AuthService authService) {
        this.tokenCookieConvertor = tokenCookieConvertor;
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoggedInMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = tokenCookieConvertor.getToken(request.getCookies());
        return authService.findLoggedInMember(token);
    }
}
