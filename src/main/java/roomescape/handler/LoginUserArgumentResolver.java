package roomescape.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.Member;
import roomescape.domain.UserRepository;
import roomescape.handler.exception.CustomException;
import roomescape.handler.exception.ExceptionCode;
import roomescape.infrastructure.TokenProvider;
import roomescape.service.dto.request.LoginUser;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String TOKEN_COOKIE_NAME = "token";

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public LoginUserArgumentResolver(TokenProvider tokenProvider, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new CustomException(ExceptionCode.BAD_REQUEST);
        }
        String token = extractTokenFromCookie(request.getCookies());
        Long userId = tokenProvider.parseSubject(token);

        Member member = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
        return LoginUser.from(member);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new IllegalArgumentException("쿠키를 찾을 수 없습니다.");
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TOKEN_COOKIE_NAME))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("쿠키를 찾을 수 없습니다."))
                .getValue();
    }
}
