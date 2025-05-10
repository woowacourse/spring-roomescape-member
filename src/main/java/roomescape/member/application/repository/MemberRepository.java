package roomescape.member.application.repository;

import java.util.Optional;
import roomescape.member.application.dto.CreateMemberRequest;
import roomescape.member.domain.Member;

public interface MemberRepository {

    Optional<Member> findByEmail(String email);

    Member insert(CreateMemberRequest request);
}
