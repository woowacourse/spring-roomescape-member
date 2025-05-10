package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Member;

public interface MemberRepository {
    Optional<Member> findMemberByEmailAndPassword(String email, String password);

    Optional<Member> findMemberById(Long id);

    List<Member> findAll();
}
