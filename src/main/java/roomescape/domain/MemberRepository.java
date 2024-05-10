package roomescape.domain;

import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findById(Long memberId);
}
