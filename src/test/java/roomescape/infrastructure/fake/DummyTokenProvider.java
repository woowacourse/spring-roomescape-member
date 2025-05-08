package roomescape.infrastructure.fake;

import roomescape.domain.AuthenticationTokenProvider;

public class DummyTokenProvider implements AuthenticationTokenProvider {

    @Override
    public String createToken(final String payload) {
        return payload;
    }

    @Override
    public String getPayload(final String token) {
        return token;
    }

    @Override
    public boolean isValidToken(final String token) {
        return true;
    }
}
