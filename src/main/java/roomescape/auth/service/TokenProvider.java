package roomescape.auth.service;

import roomescape.auth.entity.Member;

public interface TokenProvider {
    String createToken(Member member);

    String resolve(String token);
}
