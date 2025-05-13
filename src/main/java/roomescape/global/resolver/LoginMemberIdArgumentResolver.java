package roomescape.global.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.global.annotation.LoginMemberId;
import roomescape.global.security.CookieUtil;
import roomescape.global.security.JwtProvider;

@Component
public class LoginMemberIdArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtProvider jwtProvider;

    public LoginMemberIdArgumentResolver(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMemberId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = CookieUtil.extractTokenFromCookie(request.getCookies());
        return jwtProvider.getMemberId(token);
    }
}
