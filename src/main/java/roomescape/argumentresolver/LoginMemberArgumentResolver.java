package roomescape.argumentresolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.dto.login.LoginMember;
import roomescape.dto.token.TokenDto;
import roomescape.infrastructure.AuthorizationExtractor;
import roomescape.service.AuthService;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthorizationExtractor authorizationExtractor;
    private final AuthService authService;

    public LoginMemberArgumentResolver(AuthorizationExtractor authorizationExtractor, AuthService authService) {
        this.authorizationExtractor = authorizationExtractor;
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = authorizationExtractor.extractToken(request);
        return authService.checkLogin(new TokenDto(token));
    }
}
