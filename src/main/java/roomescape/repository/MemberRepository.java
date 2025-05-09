package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.business.domain.member.Member;
import roomescape.business.domain.member.MemberEmail;
import roomescape.business.domain.member.MemberName;

public interface MemberRepository {

    Member save(Member member);

    List<Member> findAll();

    Optional<Member> findById(long id);

    boolean existsByEmail(MemberEmail email);

    boolean existsByName(MemberName name);
}
