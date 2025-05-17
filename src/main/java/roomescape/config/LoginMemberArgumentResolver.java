package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
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

    private final LoginContext loginContext;

    public LoginMemberArgumentResolver(LoginContext loginContext) {
        this.loginContext = loginContext;
    }

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
        HttpServletRequest request = Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class));
        return loginContext.getLoginMember(request);
    }
}
