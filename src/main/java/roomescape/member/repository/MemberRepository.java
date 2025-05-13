package roomescape.member.repository;

import java.util.List;
import java.util.Optional;
import roomescape.member.domain.Member;

public interface MemberRepository {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByName(String name);

    boolean existsByEmailAndPassword(String email, String password);

    List<Member> findAll();

    Optional<Member> findById(long id);
}
