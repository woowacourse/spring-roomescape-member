package roomescape.global.jwt;

import roomescape.member.domain.Member;

public interface TokenProvider {

    String createToken(Member member);
    Long getInfo(String token);
}
