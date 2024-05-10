package roomescape.config;

import static roomescape.exception.ExceptionType.REQUIRED_LOGIN;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.controller.LoginMemberParameter;
import roomescape.exception.RoomescapeException;
import roomescape.service.LoginService;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final LoginService loginService;

    public LoginMemberArgumentResolver(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMemberParameter.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {
        HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String token = CookieExtractor.getCookie(servletRequest, "token")
                .orElseThrow(() -> new RoomescapeException(REQUIRED_LOGIN))
                .getValue();
        return loginService.checkLogin(token);
    }
}
