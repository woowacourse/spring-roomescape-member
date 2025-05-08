package roomescape.auth.service;

import roomescape.auth.entity.User;

public interface TokenProvider {
    String createToken(User user);

    String resolve(String token);
}
