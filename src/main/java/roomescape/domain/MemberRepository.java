package roomescape.domain;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Member;

public interface MemberRepository {
    Member save(Member member);

    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findById(Long memberId);

    List<Member> findAll();
}
