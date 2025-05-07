package roomescape.resolver;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.annotation.AuthenticationPrinciple;
import roomescape.dto.other.AuthenticationInformation;
import roomescape.exception.BadRequestException;
import roomescape.utility.JwtTokenProvider;

public class AuthenticationInformationArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationInformationArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrinciple.class);
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
        return parseAccessToken(accessToken);
    }

    private String getAccessTokenInCookie(HttpServletRequest request) {
        List<Cookie> cookies = List.of();
        if (request.getCookies() != null) {
            cookies = List.of(request.getCookies());
        }
        Optional<Cookie> accessTokenCookie = cookies.stream()
                .filter(cookie -> cookie.getName().equals("access"))
                .findFirst();
        if (accessTokenCookie.isEmpty()) {
            throw new BadRequestException("[ERROR] 인증 정보가 존재하지 않습니다.");
        }
        return accessTokenCookie.get().getValue();
    }

    private AuthenticationInformation parseAccessToken(String accessToken) {
        Claims tokenContent = jwtTokenProvider.parseToken(accessToken);
        Long id = Long.valueOf(tokenContent.getSubject());
        String name = String.valueOf(tokenContent.get("name"));
        return new AuthenticationInformation(id, name);
    }
}
