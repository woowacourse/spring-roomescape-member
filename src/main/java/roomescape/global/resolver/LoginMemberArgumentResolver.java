package roomescape.global.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.global.exception.RoomEscapeException.AuthenticationException;
import roomescape.infra.JwtTokenProvider;
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
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = extractValue(request.getCookies(), "token");
        String email = jwtTokenProvider.getPayload(token);
        User user = userDao.findByEmail(email).orElseThrow();
        return new UserPrincipal(user.getId(), user.getName(), user.getEmail(), List.of(Role.NORMAL));
    }

    private String extractValue(Cookie[] cookies, String key) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }
        throw new AuthenticationException("쿠키를 추출할 수 없습니다.");
    }
}
