package roomescape.auth.service;

import roomescape.auth.entity.Member;

public class FakeJwtTokenProvider implements TokenProvider {
    @Override
    public String createToken(Member member) {
        return null;
    }

    @Override
    public String resolve(String token) {
        return null;
    }
}
