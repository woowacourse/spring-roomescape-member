package roomescape.member.repository;

import java.util.List;
import java.util.Optional;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRole;

public interface MemberRepository {

    boolean existsByEmailAndPassword(final String email, final String password);

    Optional<Member> findMemberByEmailAndPassword(final String email, final String password);

    boolean existsByIdAndMemberRole(final Long id, final MemberRole memberRole);

    boolean existsById(final Long id);

    Optional<Member> findUserById(final Long id);

    Member save(Member member);

    List<Member> findAllUsers();
}
