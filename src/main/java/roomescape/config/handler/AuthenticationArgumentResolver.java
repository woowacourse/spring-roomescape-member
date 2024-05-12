package roomescape.config.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.exception.MemberAuthenticationException;
import roomescape.member.dto.LoggedInMember;
import roomescape.member.service.AuthService;

@Component
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String ACCESS_TOKEN_KEY = "token";

    private final AuthService authService;

    public AuthenticationArgumentResolver(AuthService authService) {
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
        String accessToken = findAccessToken(request);
        return authService.findLoggedInMember(accessToken);
    }

    private String findAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(ACCESS_TOKEN_KEY)) {
                return cookie.getValue();
            }
        }
        throw new MemberAuthenticationException();
    }
}
