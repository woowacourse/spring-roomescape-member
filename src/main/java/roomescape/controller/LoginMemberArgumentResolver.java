package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.dao.UserDao;
import roomescape.infra.JwtTokenProvider;
import roomescape.model.Role;
import roomescape.model.User;
import roomescape.model.UserPrinciple;

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
        return parameter.getParameterType().equals(UserPrinciple.class);
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
        return new UserPrinciple(user.getId(), user.getName(), user.getEmail(), List.of(Role.NORMAL));
    }

    private String extractValue(Cookie[] cookies, String key) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }
        throw new RuntimeException("cannot find key in cookies");
    }
}
