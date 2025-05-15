package roomescape.member.repository;

import java.util.List;
import java.util.Optional;
import roomescape.member.domain.Member;

public interface MemberRepository {

    boolean existsByEmail(final String email);

    Optional<Member> findMemberByEmail(final String email);

    Optional<Member> findUserById(final Long id);

    Member save(Member member);

    List<Member> findAllUsers();

    Member findById(Long id);
}
