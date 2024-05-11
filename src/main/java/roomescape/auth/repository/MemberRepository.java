package roomescape.auth.repository;

import java.util.Optional;
import roomescape.auth.domain.Member;

public interface MemberRepository {

  Optional<Member> findByEmail(String email);
}
