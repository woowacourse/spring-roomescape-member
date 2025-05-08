package roomescape.member.repository;

import java.util.Optional;
import roomescape.member.domain.Member;

public interface MemberRepository {
    Member add(Member member);

    Optional<Member> findIdByEmailAndPassword(String email, String password);
}
