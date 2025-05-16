package roomescape.auth.jwt;

import roomescape.member.domain.Member;

public interface AuthTokenProvider {

    String createTokenFromMember(Member member);
}
