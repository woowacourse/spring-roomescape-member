package roomescape.auth.resolver;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.annotation.AuthenticatedUser;
import roomescape.auth.jwt.domain.Jwt;
import roomescape.auth.jwt.domain.TokenType;
import roomescape.auth.jwt.manager.JwtManager;
import roomescape.user.domain.UserId;
import roomescape.user.domain.UserName;
import roomescape.user.domain.UserRole;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtManager jwtManager;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedUser.class)
                && parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) {

        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        final String token = Stream.of(request.getCookies())
                .filter(cookie -> TokenType.ACCESS.matches(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new MissingAccessTokenException("Missing access token from cookie"));

        final Claims claims = jwtManager.parse(Jwt.from(token));

        final Long id = claims.get("id", Long.class);
        final String name = claims.get("name", String.class);
        final String role = claims.get("role", String.class);

        return new UserSession(
                UserId.from(id),
                UserName.from(name),
                UserRole.valueOf(role));
    }
}
