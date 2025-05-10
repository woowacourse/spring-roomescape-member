package roomescape.auth.infrastructure;

import roomescape.auth.infrastructure.dto.CredentialDetails;
import roomescape.member.domain.Member;

public interface TokenProvider {

    String create(Member member);

    CredentialDetails extractToCredentialDetails(String token);
}
