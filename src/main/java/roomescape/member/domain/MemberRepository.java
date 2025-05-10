package roomescape.member.domain;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByEmailAndPassword(String email, String password);

    List<Member> getAll();
}
