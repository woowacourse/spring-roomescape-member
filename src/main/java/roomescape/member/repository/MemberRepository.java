package roomescape.member.repository;

import java.util.Optional;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberEmail;
import roomescape.member.domain.MemberId;
import roomescape.member.domain.MemberPassword;

public interface MemberRepository {
    boolean existsByEmail(MemberEmail email);

    Member save(Member member);

    Optional<Member> findById(MemberId id);

    Optional<Member> findByParams(MemberEmail email, MemberPassword password);
}
