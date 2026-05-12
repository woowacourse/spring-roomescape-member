package roomescape.global.auth.resolver;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.global.auth.UserInfo;
import roomescape.global.auth.annotation.CurrentUser;
import roomescape.global.exception.GlobalErrorCode;
import roomescape.global.exception.exception.InvalidException;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    //Todo : 인증 구현 후 UserInfo 어노테이션이 붙은 파라미터에 인증된 사용자 정보를 반환하도록 구현

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String encodedName = webRequest.getHeader("Authorization");
        String name = URLDecoder.decode(encodedName, StandardCharsets.UTF_8);

        if (name == null || name.isBlank()) {
            List<String> errors = List.of(GlobalErrorCode.AUTHENTICATION_FAILED.getMessage());
            throw new InvalidException(errors);
        }

        return new UserInfo(name);
    }
}
