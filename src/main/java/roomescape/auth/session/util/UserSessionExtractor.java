package roomescape.auth.session.util;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import roomescape.auth.jwt.domain.Jwt;
import roomescape.auth.jwt.domain.TokenType;
import roomescape.auth.jwt.manager.JwtManager;
import roomescape.auth.session.UserSession;
import roomescape.common.cookie.manager.CookieManager;
import roomescape.user.domain.UserId;
import roomescape.user.domain.UserName;
import roomescape.user.domain.UserRole;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSessionExtractor {

    public static UserSession execute(final HttpServletRequest request,
                                      final CookieManager cookieManager,
                                      final JwtManager jwtManager) {

        // TODO 쿠키
        final Jwt accessToken = Jwt.from(
                cookieManager.extractCookie(request, TokenType.ACCESS.getDescription()));

        final Claims claims = jwtManager.parse(accessToken);

        return new UserSession(
                UserId.from(claims.get(UserSession.Fields.id, Long.class)),
                UserName.from(claims.get(UserSession.Fields.name, String.class)),
                UserRole.valueOf(claims.get(UserSession.Fields.role, String.class))
        );
    }
}
