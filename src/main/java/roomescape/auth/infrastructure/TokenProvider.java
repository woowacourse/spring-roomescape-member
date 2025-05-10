package roomescape.auth.infrastructure;

import roomescape.auth.dto.AuthenticatedMember;
import roomescape.member.domain.Member;

public interface TokenProvider {

    String create(Member member);

    AuthenticatedMember resolveAuthenticatedMember(String token);
}
