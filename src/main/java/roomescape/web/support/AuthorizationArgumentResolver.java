package roomescape.web.support;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.entity.AccessToken;
import roomescape.web.CookieManager;
import roomescape.web.LoginMember;

@Component
public class AuthorizationArgumentResolver implements HandlerMethodArgumentResolver {

    private final CookieManager cookieManager;

    public AuthorizationArgumentResolver(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String tokenCookie = cookieManager.getCookieByName("token", request);

        AccessToken accessToken = new AccessToken(tokenCookie);
        return new LoginMember(
                accessToken.findSubject(),
                accessToken.findMemberName(),
                accessToken.findMemberRole()
        );
    }
}
