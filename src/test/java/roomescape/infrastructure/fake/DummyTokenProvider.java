package roomescape.infrastructure.fake;

import roomescape.domain.AuthenticationInfo;
import roomescape.domain.AuthenticationTokenProvider;
import roomescape.domain.UserRole;

public class DummyTokenProvider implements AuthenticationTokenProvider {

    private static final String SEPARATOR = ";";

    @Override
    public String createToken(final AuthenticationInfo authenticationInfo) {
        return authenticationInfo.id() + SEPARATOR + authenticationInfo.role().name();
    }

    @Override
    public long getIdentifier(final String token) {
        var authenticationInfo = getPayload(token);
        return authenticationInfo.id();
    }

    @Override
    public AuthenticationInfo getPayload(final String token) {
        var split = token.split(SEPARATOR);
        var id = Long.parseLong(split[0]);
        var role = UserRole.valueOf(split[1]);
        return new AuthenticationInfo(id, role);
    }

    @Override
    public boolean isValidToken(final String token) {
        return true;
    }
}
