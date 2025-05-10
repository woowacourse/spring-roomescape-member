package roomescape.auth.web.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.infrastructure.TokenProvider;
import roomescape.auth.annotation.AuthenticationPrincipal;
import roomescape.global.exception.AuthenticationException;
import roomescape.global.util.CookieUtils;

@Component
@RequiredArgsConstructor
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenProvider tokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Cookie cookie = CookieUtils.findFromCookiesByName(request.getCookies(), "token")
                .orElseThrow(() -> new AuthenticationException("인증을 위한 쿠키가 존재하지 않습니다."));
        //TODO : 토큰으로 추출한 memberId가 존재하는지 항상 확인하면 db에 매번 접근하게 되는데, 괜찮은걸까?
        return tokenProvider.resolveAuthenticatedMember(cookie.getValue());
    }
}
