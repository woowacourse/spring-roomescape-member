package roomescape.auth.resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.jwt.manager.JwtManager;
import roomescape.auth.session.Session;
import roomescape.auth.session.annotation.UserSession;
import roomescape.auth.session.util.UserSessionExtractor;
import roomescape.common.cookie.manager.CookieManager;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtManager jwtManager;
    private final CookieManager cookieManager;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserSession.class)
                && parameter.getParameterType().equals(Session.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) {
        return UserSessionExtractor.execute(
                (HttpServletRequest) webRequest.getNativeRequest(),
                cookieManager,
                jwtManager
        );
    }
}
