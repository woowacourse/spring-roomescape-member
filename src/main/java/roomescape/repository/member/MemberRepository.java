package roomescape.repository.member;

import roomescape.entity.member.Member;

public interface MemberRepository {

    Member findById(Long id);

    boolean existsByEmail(String email);

    Member findByEmail(String email);
}
