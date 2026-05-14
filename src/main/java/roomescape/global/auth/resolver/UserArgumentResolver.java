package roomescape.global.auth.resolver;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.global.auth.UserInfo;
import roomescape.global.auth.annotation.CurrentUser;
import roomescape.global.exception.GlobalErrorCode;
import roomescape.global.exception.exception.AuthenticationException;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && parameter.getParameterType().equals(UserInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader(AUTHORIZATION_HEADER);

        validateAuthorizationHeader(authorization);

        String encodedName = authorization.substring(AUTHORIZATION_HEADER_PREFIX.length()).trim();

        try {
            String name = URLDecoder.decode(encodedName, StandardCharsets.UTF_8);

            validateName(name);

            return new UserInfo(name);
        } catch (IllegalArgumentException e) {
            throw new AuthenticationException(GlobalErrorCode.AUTHENTICATION_FAILED.getMessage());
        }
    }

    private void validateAuthorizationHeader(final String authorization) {
        if (authorization == null || authorization.isBlank()
                || !authorization.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
            throw new AuthenticationException(GlobalErrorCode.AUTHENTICATION_FAILED.getMessage());
        }
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new AuthenticationException(GlobalErrorCode.AUTHENTICATION_FAILED.getMessage());
        }
    }
}
