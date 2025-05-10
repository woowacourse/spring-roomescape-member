package roomescape.repository.member;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;

public interface MemberRepository {

    List<Member> findAllByRole(MemberRole role);

    Optional<Member> findById(long id);

    Optional<Member> findByEmail(String email);

    long add(Member member);
}
