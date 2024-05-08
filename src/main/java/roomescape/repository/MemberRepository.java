package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Member;

public interface MemberRepository {
    Member save(Member member);

    boolean checkExistMember(String email, String password);

    Member findByEmail(String email);

    Optional<Member> findById(Long memberId);

    List<Member> findAll();
}
