package roomescape.service.tokenmanager;

import roomescape.domain.Member;

public interface TokenProvider {
    String createToken(Member member);
}
