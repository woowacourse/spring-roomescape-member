package roomescape.domain.member;

import java.util.List;
import java.util.Optional;


public interface MemberRepository {

    Member insert(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmailAndPassword(String email, String password);

    List<Member> findAll();
}
