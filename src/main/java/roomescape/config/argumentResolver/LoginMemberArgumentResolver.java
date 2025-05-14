package roomescape.config.argumentResolver;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.LoginMember;
import roomescape.jwt.JwtProvider;
import roomescape.jwt.JwtRequest;
import roomescape.util.CookieParser;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String TOKEN_NAME = "token";

    private final JwtProvider jwtProvider;

    public LoginMemberArgumentResolver(final JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Optional<String> cookieOptional = CookieParser.getCookie(request, TOKEN_NAME);

        if (cookieOptional.isEmpty()) {
            throw new JwtException("인증에 실패했습니다");
        }

        String cookie = cookieOptional.get();
        JwtRequest jwtRequest = jwtProvider.verifyToken(cookie);

        if (jwtRequest == null) {
            throw new JwtException("인증에 실패했습니다");
        }

        return new LoginMember(jwtRequest.id(), jwtRequest.name());
    }
}
