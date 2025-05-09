package roomescape.repository.member;

import roomescape.entity.Member;

public interface MemberRepository {

    Member findById(Long id);

    Member findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);
}
