package roomescape.auth.service;

import roomescape.auth.entity.User;

class FakeTokenProvider implements TokenProvider {

    @Override
    public String createToken(User user) {
        return null;
    }

    @Override
    public String resolve(String token) {
        return null;
    }
}
