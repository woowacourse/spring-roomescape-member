package roomescape.service;

import roomescape.model.User;

public interface TokenProvider {
    String createToken(User user);
}
