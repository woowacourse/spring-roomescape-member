package roomescape.repository;

import roomescape.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findById(long id);

    Optional<Member> findByEmail(String email);

    void fetchById(long id);

    Member save(Member member);
}
