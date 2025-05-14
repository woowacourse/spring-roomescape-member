package roomescape.member.repository;

import java.util.List;
import java.util.Optional;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

public interface MemberRepository {
    Member add(Member member);

    Optional<Member> findIdByEmailAndPassword(String email, String password);

    Optional<Member> findById(Long id);

    List<Member> findAll();

    Optional<Role> findRoleById(Long id);
}
