package roomescape.repository;

import java.util.Optional;
import roomescape.model.Member;

public interface MemberRepository {
    Optional<Member> findByEmail(String email);
}
