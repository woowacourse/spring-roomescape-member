package roomescape.auth;

import java.util.Objects;

import javax.naming.AuthenticationException;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import roomescape.service.UserService;

@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;
    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null || request.getCookies() == null) {
            // TODO: 예외처리
            throw new AuthenticationException();
        }
        String token = null;
        for (var cookie : request.getCookies()) {
            if (Objects.equals(cookie.getName(), "token")) {
                token = cookie.getValue();
                break;
            }
        }
        return userService.getById(Long.valueOf(jwtProvider.getPayload(token)));
    }
}
