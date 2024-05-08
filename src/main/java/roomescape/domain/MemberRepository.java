package roomescape.domain;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findById(Long id);

    List<Member> findAll();

    boolean existByEmailAndPassword(String email, String password);

    Optional<Member> findByEmail(String email);
}
