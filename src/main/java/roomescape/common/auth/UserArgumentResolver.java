package roomescape.common.auth;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.common.exception.DomainException;
import roomescape.common.exception.GlobalErrorCode;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    // 추후 로그인 도입시 변경
//    public static final String AUTHORIZATION_HEADER = "Authorization";
//    public static final String AUTHORIZATION_HEADER_PREFIX = "Bearer";

    public static final String AUTHORIZATION_HEADER = "X-Guest-Name";
    public static final String AUTHORIZATION_HEADER_PREFIX = "Guest";


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String authorization = webRequest.getHeader(AUTHORIZATION_HEADER);

        if(authorization == null || authorization.isBlank() || !authorization.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
            throw new DomainException(GlobalErrorCode.INVALID_AUTHENTICATION_HEADER);
        }

        String name = authorization.substring(AUTHORIZATION_HEADER_PREFIX.length()).trim();

        if(name.isBlank()) {
            throw new DomainException(GlobalErrorCode.INVALID_AUTHENTICATION_HEADER);
        }

        return URLDecoder.decode(name, StandardCharsets.UTF_8);
    }
}
