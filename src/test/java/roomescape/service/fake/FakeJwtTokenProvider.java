package roomescape.service.fake;

import roomescape.model.User;
import roomescape.service.TokenProvider;

public class FakeJwtTokenProvider implements TokenProvider {
    @Override
    public String createToken(User user) {
        return user.getEmail();
    }
}
