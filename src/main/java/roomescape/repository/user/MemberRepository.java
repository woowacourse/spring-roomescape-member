package roomescape.repository.user;

import roomescape.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);
}
