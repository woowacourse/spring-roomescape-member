package roomescape.common.authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.common.authorization.AuthorizationHandler;
import roomescape.common.authorization.TokenAuthorizationHandler;
import roomescape.login.service.LoginService;

@Component
public class MemberAuthenticationResolver implements HandlerMethodArgumentResolver {
    private final LoginService loginService;
    private final AuthorizationHandler<String> authorizationHandler;

    public MemberAuthenticationResolver(
            LoginService loginService,
            TokenAuthorizationHandler tokenAuthorizationHandler
    ) {
        this.loginService = loginService;
        this.authorizationHandler = tokenAuthorizationHandler;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer modelAndViewContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String token = authorizationHandler.extractToken(httpServletRequest);
        return loginService.findByToken(token);
    }
}
