package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.member.Member;

public interface MemberRepository {
    Member save(Member member);

    boolean existsByEmail(Member member);

    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findById(Long id);

    List<Member> readAll();
}
