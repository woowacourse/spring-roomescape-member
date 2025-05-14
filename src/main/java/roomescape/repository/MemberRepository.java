package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Member;
import roomescape.domain.MemberRoleType;

public interface MemberRepository {

    long insert(Member member);

    List<Member> findAllByRole(MemberRoleType role);

    Optional<Member> findById(long id);

    Optional<Member> findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);
}
