package roomescape.domain;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findByEmail(String email);
}
