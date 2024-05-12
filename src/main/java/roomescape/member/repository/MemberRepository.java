package roomescape.member.repository;

import java.util.List;
import java.util.Optional;
import roomescape.member.domain.Email;
import roomescape.member.domain.Member;

public interface MemberRepository {

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(Email email);

    List<Member> findAll();

    void delete(Long id);
}
