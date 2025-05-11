package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.custom.reason.auth.AuthNotExistsCookieException;
import roomescape.exception.custom.reason.auth.AuthNotValidTokenException;
import roomescape.member.MemberRole;

public class AuthorizationAdminInterceptor implements HandlerInterceptor {

    private static final String TOKEN_NAME = "token";

    private final JwtProvider jwtProvider;

    public AuthorizationAdminInterceptor(
            final JwtProvider jwtProvider
    ) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        final String token = extractToken(request);
        validateToken(token);

        final TokenBody tokenBody = jwtProvider.extractBody(token);
        if (tokenBody == null || !Objects.equals(tokenBody.role(), MemberRole.ADMIN)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        return true;
    }

    private String extractToken(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        validateExistsCookies(cookies);

        return Arrays.stream(cookies)
                .filter(cookie -> Objects.equals(cookie.getName(), TOKEN_NAME))
                .map(Cookie::getValue)
                .findAny()
                .orElseThrow(() -> new AuthNotExistsCookieException());
    }

    private void validateToken(final String token) {
        if(!jwtProvider.isValidToken(token)){
            throw new AuthNotValidTokenException();
        }
    }

    private void validateExistsCookies(final Cookie[] cookies) {
        if(cookies == null){
            throw new AuthNotExistsCookieException();
        }
    }
}
