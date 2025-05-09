package roomescape.presentation;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.application.AuthenticationService;
import roomescape.application.AuthorizationException;

public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthenticationService authenticationService;

    public UserArgumentResolver(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Authenticated.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        var request = (HttpServletRequest) webRequest.getNativeRequest();
        var token = ControllerSupports.findCookieValueByKey(request, "token")
            .orElseThrow(() -> new AuthorizationException("사용자 인증이 필요합니다."));
        return authenticationService.getUserByToken(token);
    }
}
