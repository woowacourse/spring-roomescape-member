package roomescape.global.auth.resolver;

import static roomescape.global.auth.CookieExtractor.extractValue;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.infra.JwtTokenProvider;
import roomescape.user.controller.AuthController;
import roomescape.user.domain.User;
import roomescape.user.domain.UserPrincipal;
import roomescape.user.service.UserService;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginMemberArgumentResolver(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = extractValue(request.getCookies(), AuthController.TOKEN_KEY);

        String email = jwtTokenProvider.getPayload(token);
        User user = userService.getUserByEmail(email);

        return UserPrincipal.from(user);
    }
}
