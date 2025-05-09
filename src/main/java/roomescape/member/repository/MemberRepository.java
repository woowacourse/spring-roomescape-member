package roomescape.member.repository;

import java.util.Optional;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberEmail;
import roomescape.member.domain.MemberPassword;

public interface MemberRepository {
    Member save(Member member);

    Optional<Member> findByParams(MemberEmail email, MemberPassword password);

    // Member findById(Long id);
}
