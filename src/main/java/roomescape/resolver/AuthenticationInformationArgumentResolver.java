package roomescape.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.annotation.AuthenticationPrinciple;
import roomescape.exception.BadRequestException;
import roomescape.utility.CookieUtility;
import roomescape.utility.JwtTokenProvider;

public class AuthenticationInformationArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtility cookieUtility;

    public AuthenticationInformationArgumentResolver(JwtTokenProvider jwtTokenProvider, CookieUtility cookieUtility) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.cookieUtility = cookieUtility;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrinciple.class)
                && parameter.getParameterType().isAssignableFrom(roomescape.dto.other.AuthenticationInformation.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = getAccessTokenInCookie(request);
        return jwtTokenProvider.parseToken(accessToken);
    }

    private String getAccessTokenInCookie(HttpServletRequest request) {
        Optional<Cookie> cookie = cookieUtility.findCookie(request, "access");
        if (cookie.isEmpty()) {
            throw new BadRequestException("[ERROR] 인증 정보가 존재하지 않습니다.");
        }
        return cookie.get().getValue();
    }
}
