package roomescape.auth.jwt;

import roomescape.member.domain.Member;

public interface AuthTokenProvider {

    String createToken(Member member);

    String extractPayload(String token);

    String extractRole(String token);
}
