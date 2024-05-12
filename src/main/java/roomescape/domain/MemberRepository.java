package roomescape.domain;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    List<Member> findAll();

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    String findNameById(Long memberId);

    Member save(Member member);
}
