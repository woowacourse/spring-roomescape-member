package roomescape.domain;

import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findMemberByEmailAndPassword(String email, String password);

    Optional<Member> findMemberById(Long memberId);
}
