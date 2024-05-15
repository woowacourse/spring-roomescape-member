package roomescape.repository;

import roomescape.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    List<Member> findAll();

    Optional<Member> findById(long id);

    Optional<Member> findByEmail(String email);

    Member save(Member member);
}
