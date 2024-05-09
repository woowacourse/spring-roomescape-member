package roomescape.domain.repository;

import java.util.Optional;

import roomescape.domain.Member;

public interface MemberRepository {
    Member save(Member user);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmailAndPassword(String email, String password);

    void deleteAll();
}
