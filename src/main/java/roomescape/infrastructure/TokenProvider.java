package roomescape.infrastructure;

import roomescape.domain.Member;

public interface TokenProvider {
    String createToken(Member member);
}
