package roomescape.config;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;
import static roomescape.config.AuthenticationInterceptor.LOGIN_MEMBER_ATTRIBUTE_NAME;

import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 로그인한 회원 정보를 컨트롤러 메서드의 인자로 주입하는 ArgumentResolver입니다.
 */
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (!parameter.hasParameterAnnotation(CurrentLoginMember.class)) {
            return false;
        }
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        return getLoginMember(webRequest);
    }

    private LoginMember getLoginMember(NativeWebRequest webRequest) {
        return (LoginMember) webRequest.getAttribute(LOGIN_MEMBER_ATTRIBUTE_NAME, SCOPE_REQUEST);
    }
}
