package roomescape.member.application.repository;

import java.util.Optional;
import roomescape.member.domain.Member;
import roomescape.member.presentation.dto.SignUpRequest;
import roomescape.member.presentation.dto.TokenRequest;

public interface MemberRepository {
    Member insert(SignUpRequest signUpRequest);

    Optional<Member> findByEmail(TokenRequest tokenRequest);
}
