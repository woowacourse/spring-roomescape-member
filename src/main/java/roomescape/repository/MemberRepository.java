package roomescape.repository;

import roomescape.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member user);

    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findById(long id);

    int deleteById(long id);
}
