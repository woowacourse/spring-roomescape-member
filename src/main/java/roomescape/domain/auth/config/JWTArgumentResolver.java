package roomescape.domain.auth.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.auth.dto.LoginUserDto;
import roomescape.domain.auth.service.AuthService;

@Component
public class JWTArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String TOKEN_NAME = "token";
    private final AuthService authService;

    public JWTArgumentResolver(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType()
                .equals(LoginUserDto.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory)
            throws Exception {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        final Cookie[] cookies = request.getCookies();

        return authService.getLoginUser(cookies);
    }
}
