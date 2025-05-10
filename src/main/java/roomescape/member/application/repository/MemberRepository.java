package roomescape.member.application.repository;

import java.util.List;
import java.util.Optional;
import roomescape.member.application.dto.CreateMemberRequest;
import roomescape.member.application.dto.GetMemberResponse;
import roomescape.member.domain.Member;

public interface MemberRepository {

    Optional<Member> findByEmail(String email);

    Member insert(CreateMemberRequest request);

    Optional<Member> findById(Long id);

    List<GetMemberResponse> findAll();
}
