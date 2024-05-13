package roomescape.repository.member;

import roomescape.domain.Email;
import roomescape.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findByEmail(Email email);

    Optional<Member> findById(Long id);

    List<Member> findAll();
}
