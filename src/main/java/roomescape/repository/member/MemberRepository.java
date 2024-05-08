package roomescape.repository.member;

import roomescape.domain.Email;
import roomescape.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findByEmail(Email email);

    List<Member> findAll();
}
