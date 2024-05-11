package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.WebUtils;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String AUTHENTICATION_INFO_KEY_NAME = "token";

    private final TokenProvider tokenProvider;

    public AuthenticationPrincipalArgumentResolver(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Cookie authenticationInfo = WebUtils.getCookie(request, AUTHENTICATION_INFO_KEY_NAME);
        if (authenticationInfo == null) {
            throw new AuthorizationException("인증 정보가 없습니다.");
        }
        String token = authenticationInfo.getValue();
        return tokenProvider.extractMemberId(token);
    }
}
