package roomescape.domain;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findByEmailAndPassword(String email, String password);
}
