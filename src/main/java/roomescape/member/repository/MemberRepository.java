package roomescape.member.repository;

import java.util.List;
import java.util.Optional;
import roomescape.member.domain.Member;

public interface MemberRepository {
    List<Member> findAll();

    Member save(Member member);

    boolean deleteById(long id);

    Optional<Member> findById(long id);

    Optional<Member> findByEmailAndPassword(String email, String password);
}
