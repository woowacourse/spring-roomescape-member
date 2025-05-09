package roomescape.repository.member;

import roomescape.entity.member.Member;

public interface MemberRepository {

    Member findById(Long id);

    boolean existsByEmailAndPassword(String email, String password);

    Member findByEmail(String email);
}
