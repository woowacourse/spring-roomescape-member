package roomescape.web.support;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.entity.AccessToken;
import roomescape.exception.InvalidAccessTokenException;
import roomescape.web.LoginMember;

@Component
public class AuthorizationArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();

        Cookie tokenCookie = null;
        if (cookies == null) {
            throw new InvalidAccessTokenException();
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                tokenCookie = cookie;
                break;
            }
        }

        if (tokenCookie == null) {
            throw new InvalidAccessTokenException();
        }

        AccessToken accessToken = new AccessToken(tokenCookie.getValue());
        return new LoginMember(
                accessToken.findSubject(),
                accessToken.findMemberName(),
                accessToken.findMemberRole()
        );
    }
}
