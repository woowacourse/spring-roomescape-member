package roomescape.repository.member;

import java.util.Optional;
import roomescape.domain.Member;

public interface MemberRepository {

    public Optional<Member> findByEmail(String email);
}
