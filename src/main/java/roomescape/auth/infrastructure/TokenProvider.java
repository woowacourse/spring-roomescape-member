package roomescape.auth.infrastructure;

import roomescape.member.domain.Member;

public interface TokenProvider {

    String create(Member member);
}
