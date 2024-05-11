package roomescape.auth;

import roomescape.model.Member;

public interface TokenProvider {

    String createToken(final Member member);

    Long getMemberId(final String token);
}
