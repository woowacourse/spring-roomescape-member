package roomescape.infrastructure.repository;

import roomescape.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByEmail(String email);
}
