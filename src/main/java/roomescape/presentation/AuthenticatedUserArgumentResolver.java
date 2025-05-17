package roomescape.presentation;

import jakarta.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.business.domain.LoginUser;
import roomescape.business.service.AuthService;

@Component
public class AuthenticatedUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public AuthenticatedUserArgumentResolver(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedUser.class) &&
               parameter.getParameterType().equals(LoginUser.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        try {
            final String token = CookieExtractor.extractToken(request);

            return authService.verifyTokenAndGetLoginUser(token);
        } catch (NoSuchElementException e) {
            return LoginUser.unknown();
        }
    }
}
