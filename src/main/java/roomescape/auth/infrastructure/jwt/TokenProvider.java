package roomescape.auth.infrastructure.jwt;

import roomescape.member.domain.Member;

public interface TokenProvider {

    String issue(Member member);

    boolean isAdmin(String token);

    Long getMemberId(String token);
}
