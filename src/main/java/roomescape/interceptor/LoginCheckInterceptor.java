package roomescape.interceptor;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.token.AuthenticationToken;
import roomescape.auth.token.TokenProvider;
import roomescape.exception.UnauthorizedException;
import roomescape.util.CookieUtil;

public class LoginCheckInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;

    public LoginCheckInterceptor(final TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        final AuthenticationToken authenticationToken = parseAuthenticationToken(request);
        validateToken(authenticationToken);

        return true;
    }

    private AuthenticationToken parseAuthenticationToken(final HttpServletRequest request) {
        final String accessToken = CookieUtil.findCookie(request, "token")
                .orElseThrow(() -> new UnauthorizedException("요청에 인증 쿠키가 존재하지 않습니다."))
                .getValue();

        if (accessToken == null || accessToken.isBlank()) {
            throw new UnauthorizedException("요청에 인증 토큰이 존재하지 않습니다.");
        }

        return tokenProvider.convertAuthenticationToken(accessToken);
    }

    private void validateToken(final AuthenticationToken authenticationToken) {
        try {
            authenticationToken.getClaims();
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException("유효하지 않은 인증 토큰입니다.");
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("만료된 인증 토큰입니다.");
        }
    }
}
