package roomescape.auth;

import java.util.Objects;

import javax.naming.AuthenticationException;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import io.jsonwebtoken.ExpiredJwtException;
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

        try {
            Long userId = Long.valueOf(jwtProvider.getPayload(token));
            return userService.getById(userId);
            // TODO: user notfound 대응 시 여기도 수정 필요
        } catch (ExpiredJwtException e) {
            // TODO: 예외 구체화 ; 핸들러에서 쿠키 제거
            throw new RuntimeException("로그아웃하세요");
        } catch (IllegalArgumentException e) {
            // TODO: 예외 구체화 고민
            throw new AuthenticationException("로그인 정보가 잘못되었습니다.");
        }
    }
}
