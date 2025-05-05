package roomescape.auth.presentation;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.application.AuthService;
import roomescape.auth.domain.User;
import roomescape.auth.exception.AuthErrorCode;
import roomescape.auth.exception.AuthorizationException;
import roomescape.auth.infrastructure.AuthorizationExtractor;

@Component
public class AuthenticatedUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthorizationExtractor<String> extractor;
    private final AuthService authService;

    public AuthenticatedUserArgumentResolver(AuthorizationExtractor<String> extractor, AuthService authService) {
        this.extractor = extractor;
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedUser.class)
                && parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String token = extractor.extract(request);
        if (token == null) {
            throw new AuthorizationException(AuthErrorCode.INVALID_TOKEN);
        }
        return authService.findUserByToken(token);
    }
}
