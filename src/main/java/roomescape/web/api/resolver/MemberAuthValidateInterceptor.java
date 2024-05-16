package roomescape.web.api.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Role;
import roomescape.web.api.token.TokenParser;
import roomescape.web.exception.AuthenticationException;
import roomescape.web.exception.AuthorizationException;

import java.util.HashMap;

@Component
public class MemberAuthValidateInterceptor extends CustomAuthInterceptor {
    private final TokenParser tokenParser;

    public MemberAuthValidateInterceptor(TokenParser tokenParser) {
        super(new HashMap<>());
        this.tokenParser = tokenParser;
    }

    @Override
    protected boolean handle(HttpServletRequest request) {
        String accessToken = tokenParser.extractToken(request.getCookies())
                .orElseThrow(AuthenticationException::new);

        Role role = tokenParser.getRole(accessToken);
        if (!role.isMember() && !role.isAdmin()) {
            throw new AuthorizationException();
        }

        return true;
    }
}
