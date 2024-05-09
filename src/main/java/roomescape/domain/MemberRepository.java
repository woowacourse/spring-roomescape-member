package roomescape.domain;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);
}
