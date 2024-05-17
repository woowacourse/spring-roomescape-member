package roomescape.web.api.resolver;

import jakarta.servlet.http.HttpServletRequest;
import roomescape.domain.member.Role;
import roomescape.web.api.token.TokenParser;
import roomescape.web.exception.AuthenticationException;
import roomescape.web.exception.AuthorizationException;

import java.util.HashMap;

public class AdminAuthValidateInterceptor extends CustomAuthInterceptor {
    private final TokenParser tokenParser;

    public AdminAuthValidateInterceptor(TokenParser tokenParser) {
        super(new HashMap<>());
        this.tokenParser = tokenParser;
    }

    @Override
    protected boolean handle(final HttpServletRequest request) {
        String token = tokenParser.extractToken(request.getCookies())
                .orElseThrow(AuthenticationException::new);

        Role role = tokenParser.getRole(token);
        if (!role.isAdmin()) {
            throw new AuthorizationException();
        }

        return true;
    }
}
