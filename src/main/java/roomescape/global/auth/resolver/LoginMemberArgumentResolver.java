package roomescape.global.auth.resolver;

import static roomescape.global.auth.CookieExtractor.extractValue;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.infra.JwtTokenProvider;
import roomescape.user.controller.AuthController;
import roomescape.user.domain.Role;
import roomescape.user.domain.User;
import roomescape.user.domain.UserPrincipal;
import roomescape.user.repository.UserDao;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginMemberArgumentResolver(UserDao userDao, JwtTokenProvider jwtTokenProvider) {
        this.userDao = userDao;
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
        User user = userDao.findByEmail(email).orElseThrow();
        return new UserPrincipal(user.getId(), user.getName(), user.getEmail(), List.of(Role.NORMAL));
    }
}
